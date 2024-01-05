package com.farmerfirst.growagric.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.farmerfirst.growagric.App;
import com.farmerfirst.growagric.MainActivity;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.callbacks.NetworkCallable;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.ActivitySplashBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.dashboard.DashboardFragment;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.message.chat.db.Chat;
import com.farmerfirst.growagric.ui.message.chat.db.ChatDatabase;
import com.farmerfirst.growagric.ui.otp.VerifyActivity;
import com.farmerfirst.growagric.ui.profile.view_model.ProfileStatusViewModel;
import com.farmerfirst.growagric.ui.register.RegisterActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.NetworkUtils;
import com.farmerfirst.growagric.utils.Utils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Splash extends AppCompatActivity {

    protected ProgressBar progressBarSplash;
    private ActivitySplashBinding binding;
    private boolean pingStatus = false;

    private ExecutorService executorService;

    int TIME_OUT = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        binding.progressBarSplash.getProgressDrawable().setColorFilter(Color.GREEN,android.graphics.PorterDuff.Mode.SRC_IN);
        binding.progressBarSplash.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorDarkOrange),PorterDuff.Mode.SRC_IN);

        LocalSharedPreferences prefs = new LocalSharedPreferences(Splash.this);
        grabFcmNotification(prefs);

        if (NetworkUtils.getInstance(Splash.this).isOnline()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetworkUtils.getInstance(Splash.this).pingUrl(IApiInterface.PING_URL);
                    executorService = Executors.newSingleThreadExecutor();
                    Future<Boolean> future = executorService.submit(new NetworkCallable(Splash.this));
                    try {
                        Boolean result = future.get();
                        if (result.equals(true)) {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                if(!prefs.getProfileInfo().equals("")) {
                                    String phone = LocalData.GetUserPhoneNumber(prefs);
                                    if (!phone.equals("")){
                                        try {
                                            checkProfileStatus(phone,prefs);
                                            isAccountVerified(phone,prefs);
                                        } catch (Exception er) {
                                            er.printStackTrace();
                                        }
                                    }
                                }else{
                                    Intent i = new Intent(Splash.this, LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }, TIME_OUT);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressBar();
                                }
                            });
                        }
                    } catch (Exception r) {
                        r.printStackTrace();
                    }
                }
            }).start();
        } else {
            hideProgressBar();
        }
    }

    private void grabFcmNotification(LocalSharedPreferences prefs){
        if(getIntent().getExtras() != null){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                if(!prefs.getProfileInfo().equals("")) {
                    if(extras.containsKey("key3") && extras.containsKey("key4")){
                        String chat_message = extras.get("key3").toString().trim();
                        String action = extras.get("key4").toString().trim();
                        if (action.equalsIgnoreCase("chat")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                            Long chat_uuid = Utils.generateRandomNumber(100000L, 999999L);
                            String user_uuid = LocalData.GetUserUUID(prefs);
                            List<Chat> CHATS;
                            int message_origin = 1;
                            CHATS = Collections.singletonList(new Chat(String.valueOf(chat_uuid), user_uuid, chat_message, message_origin, sdf.format(new Date())));
                            Executors.newSingleThreadExecutor().execute(() -> ChatDatabase.getInstance(App.getContext()).chatDao().saveAll(CHATS));
                        }
                    }
                }
            }
        }
    }

    private void isAccountVerified(String phone,LocalSharedPreferences prefs){
        HttpClient.checkAccountVerifiedStatus(phone,new ICustomResponseCallback(){
            @Override
            public void onSuccess(String value){
                try {
                    if(String.valueOf(value).contains("1")){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                navigateNextActivity(prefs);
                            }
                        },1000);
                    }else{
                        Intent i = new Intent(Splash.this,VerifyActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(){
                Intent j = new Intent(Splash.this,RegisterActivity.class);
                startActivity(j);
                finish();
            }
        });
    }

    private void checkProfileStatus(String phone,LocalSharedPreferences prefs){
        HttpClient.getProfileStatus(phone, new ICustomResponseCallback() {
            @Override
            public void onSuccess(String value) {
                prefs.deleteTrackProfileInfoUpdate();
                prefs.setTrackProfileInfoUpdate(Integer.parseInt(value));
            }
            @Override
            public void onFailure(){}
        });
    }

    private void hideProgressBar(){
        binding.progressBarSplash.setIndeterminate(false);
        binding.progressBarSplash.setVisibility(View.INVISIBLE);
        ComponentUtils.showDialog(Splash.this, "Connect to a network", "To use GrowAgric App, turn on the mobile data or connect to Wi-Fi.");
    }

    private void navigateNextActivity(LocalSharedPreferences prefs) {
        Intent intent;
        if(prefs.isLogin()){
            intent = new Intent(Splash.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            intent = new Intent(Splash.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void setProgressMax(@NonNull ProgressBar pb, int max) {
        pb.setMax(max);
    }

    private void setProgressAnimation(ProgressBar pb, int progressTo) {
        ObjectAnimator animate = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), progressTo * 100);
        animate.setDuration(500);
        animate.setInterpolator(new DecelerateInterpolator());
        animate.start();
    }
}