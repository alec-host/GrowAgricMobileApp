package com.farmerfirst.growagric.ui.register;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel{
    public MutableLiveData<String> FirstName = new MutableLiveData<>();
    public MutableLiveData<String> LastName = new MutableLiveData<>();
    public MutableLiveData<String> PhoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();
    public MutableLiveData<String> ConfirmPassword = new MutableLiveData<>();

    private MutableLiveData<RegisterUser> registerUserMutableLiveData;

    public MutableLiveData<RegisterUser> getRegisteredUser(){
        if(registerUserMutableLiveData == null){
            registerUserMutableLiveData = new MutableLiveData<>();
        }
        return registerUserMutableLiveData;
    }

    public void onClick(View view){
        RegisterUser registerUser = new RegisterUser(FirstName.getValue(),LastName.getValue(),PhoneNumber.getValue(),Password.getValue(),ConfirmPassword.getValue());
        registerUserMutableLiveData.setValue(registerUser);
    }
}
