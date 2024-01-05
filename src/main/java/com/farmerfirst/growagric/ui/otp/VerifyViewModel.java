package com.farmerfirst.growagric.ui.otp;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerifyViewModel extends ViewModel {

    public MutableLiveData<String> VerifyCode = new MutableLiveData<>();

    public MutableLiveData<Verify> VerifyMutableLiveData;

    public MutableLiveData<Verify> getVerificationCode() {
        if(VerifyMutableLiveData == null) {
            VerifyMutableLiveData = new MutableLiveData<>();
        }
        return VerifyMutableLiveData;
    }

    public void onClick(View view) {
        Verify verify = new Verify(VerifyCode.getValue());
        VerifyMutableLiveData.setValue(verify);
    }
}