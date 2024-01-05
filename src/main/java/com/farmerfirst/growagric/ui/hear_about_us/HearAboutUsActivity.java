package com.farmerfirst.growagric.ui.hear_about_us;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.databinding.ActivityHearAboutUsBinding;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.dojo.CustomAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HearAboutUsActivity extends AppCompatActivity {
    private HearAboutUsViewModel hearAboutUsViewModel;
    private ActivityHearAboutUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_hear_about_us);

        ComponentUtils.customActionBar("Select source",HearAboutUsActivity.this);

        initSpinner(getResources());

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = null;
                if(binding.spinPlatformOption.getSelectedItemPosition() == -1){
                    message = "Select a platform";
                }else if(binding.spinPlatformOption.getSelectedItem().toString().equalsIgnoreCase("other")){
                    if(binding.editOtherPlatform.getText().toString().isEmpty()){
                        message = "Enter other platform";
                        binding.editOtherPlatform.requestFocus();
                    }else{
                        message = "";
                        postdata();
                    }
                }else{
                    message = "";
                    postdata();
                }
                if(message != "")
                    //-.show snackbar.
                    ComponentUtils.showSnackBar(binding.getRoot(),message,0,R.color.colorRed);
            }
        });
    }
    public void postdata(){
        binding.buttonSubmit.showLoading();

        LocalSharedPreferences prefs = new LocalSharedPreferences(HearAboutUsActivity.this);
        String profile = prefs.getProfileInfo();
        String phoneNumber = new String();
        String farmerUUID = new String();
        String applicantName = new String();
        String platform = binding.editOtherPlatform.getText().toString();
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            JSONObject obj = new JSONObject(profile);
            farmerUUID = obj.getJSONObject("message").getString("farmer_uuid");
            applicantName = obj.getJSONObject("message").getString("first_name") +" "+obj.getJSONObject("message").getString("last_name");
            phoneNumber = obj.getJSONObject("message").getString("phone_number");

            JSONObject payload = AddAboutUsSourcePayload(farmerUUID,applicantName,phoneNumber,platform,sdf.format(new Date()));
            postHearAboutUsSource(payload,new ICustomResponseCallback(){
                @Override
                public void onSuccess(String value){}
                @Override
                public void onFailure(){}
            });
        }catch (JSONException ex){
            binding.buttonSubmit.hideLoading();
            ex.printStackTrace();
        }
    }
    private void initSpinner(Resources res){
        String[] hearAboutUsItemData = res.getStringArray(R.array.hear_about_us_platform);
        ArrayList<String> hearAboutUsItemList = new ArrayList<>(Arrays.asList(hearAboutUsItemData));
        CustomAdapter adapter = new CustomAdapter(this,hearAboutUsItemList);
        binding.spinPlatformOption.setAdapter(adapter);
        binding.spinPlatformOption.setPopupBackgroundResource(R.drawable.ga_spinner_drawable);
        binding.spinPlatformOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                if(hearAboutUsItemList.get(position).trim().equalsIgnoreCase("other")){
                    binding.textPlatformSources.setVisibility(View.VISIBLE);
                    binding.editOtherPlatform.setText("");
                    binding.editOtherPlatform.requestFocus();
                }else{
                    binding.textPlatformSources.setVisibility(View.GONE);
                    binding.editOtherPlatform.setText(binding.spinPlatformOption.getSelectedItem().toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void postHearAboutUsSource(JSONObject obj, ICustomResponseCallback iCustomResponseCallback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IApiInterface.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IApiInterface iApiInterface = retrofit.create(IApiInterface.class);

        iApiInterface.addHearAboutUsSource(obj.toString()).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response){
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        binding.buttonSubmit.hideLoading();
                        iCustomResponseCallback.onSuccess(obj.get("message").toString());
                    }else{
                        binding.buttonSubmit.hideLoading();
                    }
                }catch (Exception ex){
                    binding.buttonSubmit.hideLoading();
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t){
                binding.buttonSubmit.hideLoading();
                iCustomResponseCallback.onFailure();
                t.printStackTrace();
            }
        });
    }
    private JSONObject AddAboutUsSourcePayload(String farmerUUID,String applicantName,String phoneNumber,String source,String dateCreated){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("farmer_uuid",farmerUUID);
            object.put("full_name",applicantName);
            object.put("phone_number",phoneNumber);
            object.put("platform",source);
            object.put("date_created",dateCreated);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
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