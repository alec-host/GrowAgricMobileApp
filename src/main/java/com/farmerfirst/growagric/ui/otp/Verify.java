package com.farmerfirst.growagric.ui.otp;

public class Verify {
    private String strVerifyCode;

    public Verify(String VerifyCode){
        strVerifyCode = VerifyCode;
    }

    public String getVerifyCode() {
        return  strVerifyCode;
    }
}
