package com.farmerfirst.growagric.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.farmerfirst.growagric.MainActivity;
import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.ActivityLoginBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.firebase.FireBaseHttpPost;
import com.farmerfirst.growagric.firebase.FireBaseJson;
import com.farmerfirst.growagric.ui.change_password.ChangePasswordActivity;
import com.farmerfirst.growagric.ui.change_password.SendCodeActivity;
import com.farmerfirst.growagric.ui.login.data.ClientData;
import com.farmerfirst.growagric.ui.otp.VerifyActivity;
import com.farmerfirst.growagric.ui.register.RegisterActivity;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;
import com.farmerfirst.growagric.utils.NetworkUtils;
import com.farmerfirst.growagric.utils.Utils;

import org.json.JSONObject;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);

        AndroidBug5497Workaround.assistActivity(this);

        ComponentUtils.showStatusBar(LoginActivity.this);

        if(!NetworkUtils.getInstance(LoginActivity.this).isOnline()) {
            String message="No internet connection";
            ComponentUtils.hideKeyboard(LoginActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(),message,0,R.color.colorRed);
        }

        handleEditInputValidation();

        loginViewModel.getUser().observe(this, new Observer<LoginUser>() {
            @Override
            public void onChanged(LoginUser loginUser){
                String message = new String();
                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrPhoneNumber())) {
                    message = "Enter phone number";
                    binding.phone.requestFocus();
                }else if(!loginUser.getStrPhoneNumber().startsWith("0") && loginUser.getStrPhoneNumber().length() <= 10){
                    message = "Invalid phone number";
                    binding.phone.requestFocus();
                }else if(loginUser.getStrPhoneNumber().startsWith("0") && loginUser.getStrPhoneNumber().length() < 10){
                    message = "Invalid phone number";
                    binding.phone.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(loginUser).getStrPassword())){
                    message = "Enter password";
                    binding.password.requestFocus();
                }else if(!loginUser.isPasswordLengthGreaterThan5()){
                    message = "Enter at least 6 digits password";
                    binding.password.requestFocus();
                }else {

                    message = "";

                    LocalSharedPreferences prefs = new LocalSharedPreferences(getApplicationContext());

                    binding.loginButton.showLoading();

                    String code = binding.countryCodeHolder.getSelectedCountryCode().toString();
                    String phone = Utils.removeLeadZero(binding.phone.getText().toString());
                    String password = binding.password.getText().toString();
                    String phone_number = code+phone;
                    JSONObject obj = ClientData.signInPayload(phone_number,password);

                    checkProfileStatus(phone_number,prefs);

                    userLogin(obj,phone_number,prefs);
                }
                if(message.trim().length() > 0){
                    ComponentUtils.hideKeyboard(LoginActivity.this);
                    ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
                }
            }
        });

        binding.signUpCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent k = new Intent(LoginActivity.this, SendCodeActivity.class);
                k.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(k);
            }
        });
    }

    private void userLogin(JSONObject obj,String phone_number,LocalSharedPreferences prefs){
        HttpClient.authenticateUser(obj,new ICustomResponseCallback() {
            @Override
            public void onSuccess(String value) {
                try {
                    JSONObject obj = new JSONObject(value);
                    if(obj.get("success").toString() == "true"){
                        prefs.setIsLogin(true);
                        prefs.setUserToken(obj.toString());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                storeUserProfileInfo(phone_number,prefs);
                                binding.phone.setText(null);
                                binding.password.setText(null);
                            }
                        },1000);
                        try {
                            binding.loginButton.hideLoading();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        finish();
                    }else{
                        binding.loginButton.hideLoading();
                        ComponentUtils.hideKeyboard(LoginActivity.this);
                        ComponentUtils.showSnackBar(binding.getRoot(), obj.get("message").toString(),0,R.color.colorRed);
                    }
                }catch (Exception exx){
                    try {
                        binding.loginButton.hideLoading();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure() {
                try {
                    binding.loginButton.hideLoading();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private void storeUserProfileInfo(String phone,LocalSharedPreferences prefs){
        HttpClient.getUserProfile(phone,new ICustomResponseCallback() {
            @Override
            public void onSuccess(String value) {
                try {
                    prefs.deleteProfileInfo();
                    prefs.setProfileInfo(value.toString());
                    synchFCMToken(prefs);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure() {}
        });
    }

    private void handleEditInputValidation(){
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

    private void synchFCMToken(LocalSharedPreferences prefs){
        if(NetworkUtils.getInstance(getApplication()).isOnline()) {
            prefs = new LocalSharedPreferences(LoginActivity.this);
            FireBaseHttpPost httpPostpost = new FireBaseHttpPost();
            String uuid = LocalData.GetUserUUID(prefs);
            JSONObject obj = FireBaseJson.data(prefs.getFcmToken());
            if (obj != null) {
                httpPostpost.firebaseToken(uuid,obj,new ICustomResponseCallback() {
                    @Override
                    public void onSuccess(String value) {
                    }
                    @Override
                    public void onFailure() {
                    }
                });
            }
        }
    }
}