package com.farmerfirst.growagric.ui.message.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.databinding.ActivityChatBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.message.chat.db.Chat;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.NetworkUtils;
import com.farmerfirst.growagric.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.OnDeleteButtonListener,ChatAdapter.ChatAdapterListener{
    private ActivityChatBinding binding;
    private ChatAdapter chatAdapter;
    private ChatAndroidViewModel chatAndroidViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ChatActivity.this,R.layout.activity_chat);

        ComponentUtils.customActionBar("Chat",ChatActivity.this);

        AndroidBug5497Workaround.assistActivity(ChatActivity.this);

        binding.chatRelativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Rect r = new Rect();
                        binding.chatRelativeLayout.getWindowVisibleDisplayFrame(r);
                        int screenHeight = binding.chatRelativeLayout.getContext().getResources().getDisplayMetrics().heightPixels;
                        int keypadHeight = screenHeight -  r.bottom;
                        if(keypadHeight != 0){
                            keepRecyclerviewScrolledToBottom();
                        }
                    }
                },100);

            }
        });

        setupRecyclerView();

        binding.messageInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.chatRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        keepRecyclerviewScrolledToBottom();
                    }
                },200);
                return false;
            }
        });

        binding.buttonSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendChatMessage();
                binding.messageInput.setText(null);
            }
        });
    }

    private void setupRecyclerView(){
        LocalSharedPreferences prefs = new LocalSharedPreferences(ChatActivity.this);
        chatAdapter = new ChatAdapter(ChatActivity.this,this,this);
        chatAndroidViewModel = new ViewModelProvider(this).get(ChatAndroidViewModel.class);
        chatAndroidViewModel.getAllChats(LocalData.GetUserUUID(prefs)).observe(this,chats -> chatAdapter.setData(chats));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(mLayoutManager);
        binding.chatRecyclerView.setHasFixedSize(true);
        binding.chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.chatRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL){
            @Override
            public void onDraw(Canvas c,RecyclerView parent,RecyclerView.State state){

            }
        });
        binding.chatRecyclerView.setAdapter(chatAdapter);
        binding.chatRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                keepRecyclerviewScrolledToBottom();
            }
        },200);
    }

    private void sendChatMessage(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        LocalSharedPreferences prefs = new LocalSharedPreferences(ChatActivity.this);
        String chatMessage = binding.messageInput.getText().toString().trim();
        String uuid = LocalData.GetUserUUID(prefs).toString();
        int message_origin = 0;
        long rndDigit = Utils.generateRandomNumber(10000000L, 99999999L);
        if(chatMessage.length() > 0) {
            storeToDB(String.valueOf(rndDigit),uuid,chatMessage.trim(),message_origin,sdf.format(new Date()));
            httpPostMessage(uuid,chatMessage,new ICustomResponseCallback(){
                @Override
                public void onSuccess(String value){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            keepRecyclerviewScrolledToBottom();
                        }
                    },200);
                }
                @Override
                public void onFailure() {}
            });
        }
    }

    public void keepRecyclerviewScrolledToBottom(){
        int totalItemCount = binding.chatRecyclerView.getAdapter().getItemCount();
        binding.chatRecyclerView.scrollToPosition(totalItemCount-1);
    }

    private void httpPostMessage(String uuid,String message,ICustomResponseCallback iCustomResponseCallback){

        JSONObject objPayload = ChatPayload(uuid,message);

        final IApiInterface iApiInterface = RetrofitClient.getApiService();

        iApiInterface.sendMessage(objPayload.toString()).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                try {
                    if (response.code() == 200 || response.code() == 201){
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.get("message").toString());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }

    private JSONObject ChatPayload(String farmerUUID,String message){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("farmer_uuid", farmerUUID);
            object.put("message", message.trim());
            object.put("message_origin", "0");
            return object;
        }catch (JSONException e){
            return null;
        }
    }

    private void storeToDB(String chatUUID,String farmerUUID,String message,int messageOrigin,String date){
        chatAndroidViewModel = new ViewModelProvider(this).get(ChatAndroidViewModel.class);
        chatAndroidViewModel.saveChat(new Chat(chatUUID,farmerUUID,message,messageOrigin,date));
    }

    @Override
    public void onDeleteButtonClicked(Chat chat){
        chatAndroidViewModel.deleteChat(chat);
    }
    @Override
    public void onChatClick(String farmer_uuid){}
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}