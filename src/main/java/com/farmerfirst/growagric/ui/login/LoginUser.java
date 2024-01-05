package com.farmerfirst.growagric.ui.login;


import android.util.Patterns;

public class LoginUser {

    private String strPhoneNumber;
    private String strPassword;

    public LoginUser(String PhoneNumber, String Password) {
        strPhoneNumber = PhoneNumber;
        strPassword = Password;
    }

    public String getStrPhoneNumber() {
        return strPhoneNumber;
    }

    public String getStrEmailAddress(){
        return "";
    }

    public String getStrPassword() {
        return strPassword;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getStrEmailAddress()).matches();
    }

    public boolean isPhoneNumberStartWithZero(){
        return  getStrPhoneNumber().startsWith("0");
    }

    public boolean isPasswordLengthGreaterThan5() {
        return getStrPassword().length() > 4;
    }
}