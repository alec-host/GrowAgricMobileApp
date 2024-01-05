package com.farmerfirst.growagric.ui.otp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.ActivityVerifyBinding;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.otp.data.ClientData;
import com.farmerfirst.growagric.utils.ComponentUtils;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VerifyActivity extends AppCompatActivity {

    private ActivityVerifyBinding binding;
    private VerifyViewModel verifyViewModel;
    private Intent intent;
    private boolean isResendOTPButtonDisabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyViewModel = new ViewModelProvider(this).get(VerifyViewModel.class);
        binding = DataBindingUtil.setContentView(VerifyActivity.this,R.layout.activity_verify);

        binding.setLifecycleOwner(this);
        binding.setVerifyViewModel(verifyViewModel);

        ComponentUtils.showStatusBar(VerifyActivity.this);

        intent = getIntent();

        LocalSharedPreferences prefs = new LocalSharedPreferences(VerifyActivity.this);

        resendVerificationCode(prefs);

        verifyViewModel.getVerificationCode().observe(this, new Observer<Verify>() {
            @Override
            public void onChanged(Verify verify) {
                String message = new String();
                if(TextUtils.isEmpty(Objects.requireNonNull(verify).getVerifyCode())) {
                    message = "Enter verification code";
                    binding.editVerificationCode.requestFocus();
                }else if(binding.editVerificationCode.getText().toString().length() < 6){
                    message = "Invalid verification code";
                }else{
                    message = "";
                    binding.confirmPhoneNumberButton.showLoading();
                    String phoneNumber = prefs.getPhoneNo();
                    String verificationCode = binding.editVerificationCode.getText().toString();

                    JSONObject verifyjPayload = ClientData.verifyPhonePayload(phoneNumber,verificationCode);

                    HttpClient.verifyPhoneNumber(verifyjPayload, new ICustomResponseCallback() {
                        @Override
                        public void onSuccess(String value){
                            if(value.toLowerCase().contains("invalid") || value.toLowerCase().contains("no otp") || value.toLowerCase().contains("does not exist")){
                                binding.editVerificationCode.setText(null);
                                binding.confirmPhoneNumberButton.hideLoading();
                                ComponentUtils.hideKeyboard(VerifyActivity.this);
                                ComponentUtils.showSnackBar(binding.getRoot(), value, 0, R.color.colorRed);
                            }else {
                                binding.confirmPhoneNumberButton.hideLoading();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.editVerificationCode.setText("");
                                        Intent j = new Intent(VerifyActivity.this, LoginActivity.class);
                                        startActivity(j);
                                        finish();
                                    }
                                }, 100);
                            }
                        }
                        @Override
                        public void onFailure(){
                            binding.confirmPhoneNumberButton.hideLoading();
                        }
                    });
                }
                if(message.length() > 0){
                    ComponentUtils.hideKeyboard(VerifyActivity.this);
                    ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
                }
            }
        });
    }

    private void resendVerificationCode(LocalSharedPreferences prefs) {
        binding.textButtonResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isResendOTPButtonDisabled) {
                    if (intent != null) {
                        String phoneNumber = prefs.getPhoneNo();
                        if (phoneNumber != null) {
                            HttpClient.verificationCodeRequest(phoneNumber, new ICustomResponseCallback() {
                                @Override
                                public void onSuccess(String value) {
                                }
                                @Override
                                public void onFailure() {
                                }
                            });
                        } else {
                            System.out.println("phone no has not been SET");
                        }
                    }
                    disableResendOTPButtonForOneMinute();
                }
            }
        });
    }

    private void disableResendOTPButtonForOneMinute() {
        isResendOTPButtonDisabled = true;
        binding.textButtonResend.setEnabled(false);
        new CountDownTimer(31000,1000){
            public void onTick(long remainingMilliseconds){
                binding.textButtonResend.setText("Wait " + remainingMilliseconds/1000 + " seconds");
            }
            public void onFinish(){
                isResendOTPButtonDisabled = false;
                binding.textButtonResend.setEnabled(true);
                binding.textButtonResend.setText("Resend");
            }
        }.start();
    }
}