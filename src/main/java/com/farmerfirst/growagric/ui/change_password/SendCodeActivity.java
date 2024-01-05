package com.farmerfirst.growagric.ui.change_password;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.ActivitySendCodeBinding;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.Utils;

import java.util.Objects;

public class SendCodeActivity extends AppCompatActivity {
    private ActivitySendCodeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(SendCodeActivity.this,R.layout.activity_send_code);

        AndroidBug5497Workaround.assistActivity(this);

        ComponentUtils.showStatusBar(SendCodeActivity.this);

        handlePhoneOnChangeValidation();

        binding.btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP();
            }
        });
    }

    private void sendOTP(){
        String message = validateInput();
        if(message.length() > 0){
            ComponentUtils.hideKeyboard(SendCodeActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(),message, 0, R.color.colorRed);
        }else{
            String code = binding.countryCodeHolder.getSelectedCountryCode().toString();
            String phoneNumber = Utils.removeLeadZero(binding.phone.getText().toString());
            binding.btnSendCode.showLoading();
            if(phoneNumber != null) {
                HttpClient.verificationCodeRequest(code+phoneNumber,new ICustomResponseCallback() {
                    @Override
                    public void onSuccess(String value){
                        if(value.length() > 6) {
                            binding.btnSendCode.hideLoading();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(SendCodeActivity.this, ChangePasswordActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(i);
                                }
                            },50);
                        }
                    }
                    @Override
                    public void onFailure(){
                        binding.btnSendCode.hideLoading();
                    }
                });
            }
        }
    }

    private String validateInput(){
        String message = new String();
        if (TextUtils.isEmpty(binding.phone.getText().toString())) {
            message = "Enter phone number";
            binding.phone.requestFocus();
        }else if(!binding.phone.getText().toString().startsWith("0") && binding.phone.getText().toString().length() <= 10){
            message = "Invalid phone number";
            binding.phone.requestFocus();
        }else if(binding.phone.getText().toString().startsWith("0") && binding.phone.getText().toString().length() < 10) {
            message = "Invalid phone number";
            binding.phone.requestFocus();
        }

        return message;
    }

    private void handlePhoneOnChangeValidation(){
        binding.phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(!x.startsWith("0") && x.length() <= 10) {
                    binding.phone.setError("Invalid phone number");
                }else if(x.startsWith("0") && x.length() < 10){
                    binding.phone.setError("Invalid phone number");
                }else{
                    binding.phone.setError(null);
                }
            }
        });
    }
    private void navigation(){
        finish();
        Intent i=new Intent(SendCodeActivity.this, LoginActivity.class);
        overridePendingTransition(0,0);
        startActivity(i);
        overridePendingTransition(0,0);
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        navigation();
    }
    @Override
    public boolean onSupportNavigateUp(){
        navigation();
        return true;
    }
}