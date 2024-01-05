package com.farmerfirst.growagric.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.api.http.HttpClient;
import com.farmerfirst.growagric.databinding.ActivityRegisterBinding;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.login.LoginActivity;
import com.farmerfirst.growagric.ui.otp.VerifyActivity;
import com.farmerfirst.growagric.ui.register.data.ClientData;
import com.farmerfirst.growagric.ui.softkeyboardadjust.AndroidBug5497Workaround;
import com.farmerfirst.growagric.utils.ComponentUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);

        binding.setLifecycleOwner(this);
        binding.setRegisterViewModel(registerViewModel);

        AndroidBug5497Workaround.assistActivity(this);

        ComponentUtils.showStatusBar(RegisterActivity.this);

        initButton();

        handleEditInputValidation();

        registerViewModel.getRegisteredUser().observe(this, new Observer<RegisterUser>(){
            @Override
            public void onChanged(RegisterUser registerUser){
                String message = new String();
                if(TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrFirstName())) {
                    message = "Enter first name";
                    binding.editTextFirstName.requestFocus();
                }else if(registerUser.getStrFirstName().length() < 3){
                    message = "Invalid first name";
                }else if(TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrLastName())){
                    message = "Enter last name";
                    binding.editTextLastName.requestFocus();
                }else if(registerUser.getStrLastName().length() < 3){
                    message = "Invalid last name";
                }else if(TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrPhoneNumber())){
                    message = "Enter phone number";
                    binding.editTextMobileNumber.requestFocus();
                }else if(!registerUser.getStrPhoneNumber().startsWith("0") && registerUser.getStrPhoneNumber().length() <= 10){
                    message = "Invalid phone number";
                    binding.editTextMobileNumber.requestFocus();
                }else if(registerUser.getStrPhoneNumber().startsWith("0") && registerUser.getStrPhoneNumber().length() < 10){
                    message = "Invalid phone number";
                    binding.editTextMobileNumber.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrPassword())){
                    message = "Enter password";
                    binding.editTextPassword.requestFocus();
                }else if(!registerUser.isPasswordLengthGreaterThan5()){
                    message = "Enter at least 6 digits password";
                    binding.editTextPassword.requestFocus();
                }else if(TextUtils.isEmpty(Objects.requireNonNull(registerUser).getStrConfirmPassword())){
                    message = "Confirm password";
                }else if(registerUser.isMismatchingPassword() == false) {
                    message = "Mismatching password";
                    binding.editTextPassword.requestFocus();
                }else{

                    message = "";

                    binding.buttonSubmit.showLoading();

                    LocalSharedPreferences prefs = new LocalSharedPreferences(getApplicationContext());

                    String firstName = binding.editTextFirstName.getText().toString();
                    String lastName = binding.editTextLastName.getText().toString();
                    String password = binding.editTextPassword.getText().toString();
                    String confirmPassword = binding.editTextConfirmPassword.getText().toString();

                    String phoneNumber = binding.editTextMobileNumber.getText().toString();
                    if (phoneNumber.charAt(0) == '0') {
                        phoneNumber = "254" + Integer.toString(Integer.parseInt(phoneNumber));
                        if(prefs.getPhoneNo()!=""){
                            prefs.deletePhoneNo();
                        }
                        prefs.setPhoneNo(phoneNumber);
                    }

                    JSONObject obj = ClientData.signUpPayload(firstName,lastName,phoneNumber,password,confirmPassword);

                    HttpClient.registerNewUser(obj, new ICustomResponseCallback() {
                        @Override
                        public void onSuccess(String value) {
                            binding.buttonSubmit.hideLoading();
                            try {
                                JSONObject obj = new JSONObject(value);
                                if(obj.get("success").toString().equalsIgnoreCase("false")) {
                                    ComponentUtils.hideKeyboard(RegisterActivity.this);
                                    ComponentUtils.showSnackBar(binding.getRoot(),obj.get("message").toString(), 0, R.color.colorRed);
                                }else{
                                    prefs.setProfileInfo(value);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            startActivity(intent);
                                            finish();
                                        }
                                    },100);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure() {
                            binding.buttonSubmit.hideLoading();
                        }
                    });

                    /*
                    final IApiInterface iApiInterface = RetrofitClient.getApiService();

                    iApiInterface.register(obj.toString()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try{
                                if(response.code() == 200 || response.code() == 201) {
                                    String responseBody = response.body().string();
                                    JSONObject obj = new JSONObject(responseBody);
                                    //-.store profile data.
                                    prefs.setProfileInfo(obj.toString());
                                    binding.buttonSubmit.hideLoading();
                                    //-.navigation.
                                    Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    String responseBody = "{\"success\":\"false\",\"error\":\"true\",\"message\":\"something wrong has happened\"}";
                                    JSONObject obj = new JSONObject(responseBody);
                                    binding.buttonSubmit.hideLoading();
                                }
                            }catch (Exception e){
                                binding.buttonSubmit.hideLoading();
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            binding.buttonSubmit.hideLoading();
                            t.printStackTrace();
                        }
                    });*/
                }
                //-.show bar.
                if(message.trim().length() > 0) {
                    ComponentUtils.hideKeyboard(RegisterActivity.this);
                    ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
                }
            }
        });
    }

    private void handleEditInputValidation(){
        binding.editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() < 3){
                    binding.editTextFirstName.setError("Invalid first name");
                }else{
                    binding.editTextFirstName.setError(null);
                }
            }
        });
        binding.editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(x.length() < 3){
                    binding.editTextLastName.setError("Invalid last name");
                }else{
                    binding.editTextLastName.setError(null);
                }
            }
        });
        binding.editTextMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if(!x.startsWith("0") && x.length() <= 10) {
                    binding.editTextMobileNumber.setError("Invalid phone number");
                }else if(x.startsWith("0") && x.length() < 10){
                    binding.editTextMobileNumber.setError("Invalid phone number");
                }else{
                    binding.editTextMobileNumber.setError(null);
                }
            }
        });
    }

    private void initButton(){
        binding.signInCaption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });

        binding.textReadTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customBottomDialog();
            }
        });

        binding.checkBoxReadTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(buttonView.isChecked()){
                            binding.buttonSubmit.setEnabled(true);
                        }else{
                            binding.buttonSubmit.setEnabled(false);
                        }
                    }
                },500);
            }
        });
    }

    private void customBottomDialog(){
        try {
            Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_list_dialog);
            WebView wv = dialog.findViewById(R.id.webViewBottom);
            AppCompatButton buttonClose = dialog.findViewById(R.id.buttonClose);
            AssetManager manager = getAssets();
            InputStream stream = manager.open("terms.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }


            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            wv.getSettings().setSupportZoom(false);
            wv.setHorizontalScrollBarEnabled(true);

            wv.loadDataWithBaseURL(null, builder.toString(), "text/html", "UTF-8", null);

            dialog.setCancelable(false);
            dialog.show();

            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }catch(Exception ess){
            ess.printStackTrace();
        }
    }
}