package com.farmerfirst.growagric.ui.invite;

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
import com.farmerfirst.growagric.databinding.ActivityInviteBinding;
import com.farmerfirst.growagric.datastore.LocalData;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;
import com.farmerfirst.growagric.ui.invite.data.ClientData;
import com.farmerfirst.growagric.ui.record_keeping.input.ChickenActivity;
import com.farmerfirst.growagric.utils.ComponentUtils;

import org.json.JSONObject;

public class InviteActivity extends AppCompatActivity {
    private ActivityInviteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_invite);

        ComponentUtils.customActionBar("Invite farmer",InviteActivity.this);

        LocalSharedPreferences prefs = new LocalSharedPreferences(InviteActivity.this);

        inputCheck();

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClientData(LocalData.GetUserUUID(prefs));
            }
        });
    }

    private void inputCheck() {
        binding.editFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if (x.length() <= 3) {
                    binding.editFullName.setError("Invalid name");
                } else {
                    binding.editFullName.setError(null);
                }
            }
        });

        binding.editPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                String x = editable.toString();
                if (!x.startsWith("0") && x.length() <= 10) {
                    binding.editPhoneNumber.setError("Invalid phone number");
                } else if (x.startsWith("0") && x.length() < 10) {
                    binding.editPhoneNumber.setError("Invalid phone number");
                } else {
                    binding.editPhoneNumber.setError(null);
                }
            }
        });
    }

    private String validateInput(){
        String message = new String();
        if(TextUtils.isEmpty(binding.editFullName.getText())){
            message = "Enter name";
            binding.editFullName.requestFocus();
        }else if(binding.editFullName.getText().toString().length() < 3){
            message = "Enter name";
        } else if(TextUtils.isEmpty(binding.editPhoneNumber.getText())){
            message = "Enter phone number";
            binding.editPhoneNumber.requestFocus();
        }else if(!binding.editPhoneNumber.getText().toString().startsWith("0") && binding.editPhoneNumber.getText().toString().length() <= 10){
            message = "Invalid phone number";
            binding.editPhoneNumber.requestFocus();
        }else if(binding.editPhoneNumber.getText().toString().startsWith("0") && binding.editPhoneNumber.getText().toString().length() < 10){
            message = "Invalid phone number";
        } else {
            message = "";
        }
        return message;
    }

    private void postClientData(String user_uuid){
        String message = validateInput();

        if(message.trim().length() == 0){
            String inviteeName = binding.editFullName.getText().toString();
            String inviteePhone = binding.editPhoneNumber.getText().toString();
            binding.buttonSubmit.showLoading();

            JSONObject obj = ClientData.InvitePayload(inviteeName,inviteePhone,user_uuid);

            HttpClient.PostInvite(obj, new ICustomResponseCallback() {
                @Override
                public void onSuccess(String value) {
                    try {
                        if (value.length() > 0) {
                            try {
                                JSONObject obj = new JSONObject(value);
                            }catch(Exception ex){
                                binding.buttonSubmit.hideLoading();
                            }
                        } else {
                            binding.buttonSubmit.hideLoading();
                        }
                    }catch (Exception ex){
                        binding.buttonSubmit.hideLoading();
                        ex.printStackTrace();
                    }
                }
                @Override
                public void onFailure() {
                    binding.buttonSubmit.hideLoading();
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.editFullName.setText(null);
                    binding.editFullName.setError(null);
                    binding.editPhoneNumber.setText(null);
                    binding.editPhoneNumber.setError(null);
                    binding.buttonSubmit.hideLoading();
                }
            },1000);
        }else{
            ComponentUtils.hideKeyboard(InviteActivity.this);
            ComponentUtils.showSnackBar(binding.getRoot(), message, 0, R.color.colorRed);
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