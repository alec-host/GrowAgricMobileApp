package com.farmerfirst.growagric.ui.register;

public class RegisterUser {

    private String strFirstName;
    private String strLastName;
    private String strPhoneNumber;
    private String strPassword;
    private String strConfirmPassword;
    private Boolean boolCheckBox;

    public RegisterUser(String FirstName,String LastName,String PhoneNumber,String Password,String ConfirmPassword){
        strFirstName = FirstName;
        strLastName = LastName;
        strPhoneNumber = PhoneNumber;
        strPassword = Password;
        strConfirmPassword = ConfirmPassword;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public String getStrPhoneNumber() {
        return strPhoneNumber;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public String getStrConfirmPassword() {
        return strConfirmPassword;
    }

    public boolean isPasswordLengthGreaterThan5() {
        return getStrPassword().length() > 5;
    }

    public boolean isMismatchingPassword() {
        if(getStrPassword().trim().equals(getStrConfirmPassword().trim())){
            return true;
        }else{
            return false;
        }
    }
}