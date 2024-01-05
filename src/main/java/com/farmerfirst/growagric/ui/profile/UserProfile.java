package com.farmerfirst.growagric.ui.profile;

public class UserProfile {
    private String strIDNumber;
    private String strEmail;
    private String strGender;
    private String strAge;
    private String strMarried;
    private String strLevelOfEducation;
    private String strHomeCounty;
    private String strFirstName;
    private String strLastName;

    public UserProfile(String IDNumber,String Email,String Gender,String Age,String Married,String LevelOfEducation,String HomeCounty){
        this.strIDNumber = IDNumber;
        this.strEmail = Email;
        this.strGender = Gender;
        this.strAge = Age;
        this.strMarried = Married;
        this.strLevelOfEducation = LevelOfEducation;
        this.strHomeCounty = HomeCounty;
    }

    public String getStrIDNumber() {
        return strIDNumber;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public String getStrGender() {
        return strGender;
    }

    public void setStrAge(String strAge) {
        this.strAge = strAge;
    }

    public String getStrMarried() {
        return strMarried;
    }

    public String getStrLevelOfEducation() {
        return strLevelOfEducation;
    }

    public String getStrHomeCounty() {
        return strHomeCounty;
    }
}