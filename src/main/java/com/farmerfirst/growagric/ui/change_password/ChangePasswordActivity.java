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
import com.farmerfirst.growagric.databinding.ActivityChangePasswordBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.change_password.data.ClientData;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.otp.VerifyActivity;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;

import org.json.JSONObject;

import java.util.Objects;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(ChangePasswordActivity.this,R.layout.activity_change_password);

        AndroidBug5497Workaround.assistActivity(this);

        ComponentUtils.showStatusBar(ChangePasswordActivity.this);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocalSharedPreferences prefs = new LocalSharedPreferences(ChangePasswordActivity.this);
                submitPasswordChange(prefs);
            }
        });
    }

    public void submitPasswordChange(LocalSharedPreferences prefs){
        String message = validateInput();
        if(message.length()>0){
            ComponentUtils.hideKeyboard(ChangePasswordActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(),message,0,R.color.colorRed);
        }else{
            String password = binding.password.getText().toString();
            String confirmPassword = binding.confirmPassword.getText().toString();
            String verificationCode = binding.pinView.getText().toString();
            binding.btnSubmit.showLoading();
            JSONObject obj = ClientData.changePasswordPayload(LocalData.GetUserUUID(prefs),password,confirmPassword,verificationCode);
            HttpClient.forgotPassword(obj,new ICustomResponseCallback() {
                @Override
                public void onSuccess(String value){
                    if(value.toLowerCase().contains("password")) {
                        binding.btnSubmit.hideLoading();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(i);
                                finish();
                            }
                        },50);
                    }
                }
                @Override
                public void onFailure() {
                    binding.btnSubmit.hideLoading();
                }
            });
        }
    }

    private void editOnChangeValidation(){
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() < 6) {
                    binding.password.setError("Invalid password");
                }else{
                    binding.password.setError(null);
                }
            }
        });
        binding.confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() < 6) {
                    binding.confirmPassword.setError("Invalid password");
                }else{
                    binding.confirmPassword.setError(null);
                }
            }
        });
    }

    private String validateInput(){
        String message = new String();
        if(TextUtils.isEmpty(binding.password.getText())){
            message = "Enter password";
            binding.password.requestFocus();
        }else if(binding.password.getText().toString().length() < 6){
            message = "Enter at least 6 digits password";
            binding.password.requestFocus();
        }else if(TextUtils.isEmpty(binding.confirmPassword.getText())){
            message = "Confirm password";
        }else if(!binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())){
            message = "Mismatching password";
            binding.password.requestFocus();
        }else if(binding.pinView.getText().toString().length() < 6){
            message = "Invalid verification code";
            binding.pinView.requestFocus();
        }
        return message;
    }
    private void navigation(){
        finish();
        Intent i=new Intent(ChangePasswordActivity.this,SendCodeActivity.class);
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