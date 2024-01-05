package com.farmerfirst.growagric.ui.profile;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    public MutableLiveData<String> IDNumber = new MutableLiveData<>();
    public MutableLiveData<String> Email = new MutableLiveData<>();
    public MutableLiveData<String> Gender = new MutableLiveData<>();
    public MutableLiveData<String> Age = new MutableLiveData<>();
    public MutableLiveData<String> Married = new MutableLiveData<>();
    public MutableLiveData<String> LevelOfEducation = new MutableLiveData<>();
    public MutableLiveData<String> HomeCounty = new MutableLiveData<>();

    private MutableLiveData<UserProfile> profileMutableLiveData;

    public MutableLiveData<UserProfile> getProfile(){
        if(profileMutableLiveData == null){
            profileMutableLiveData = new MutableLiveData<>();
        }
        return profileMutableLiveData;
    }

    public void onClick(View view){
        UserProfile profile = new UserProfile(IDNumber.getValue(),Email.getValue(),Gender.getValue(),Age.getValue(),Married.getValue(),LevelOfEducation.getValue(),HomeCounty.getValue());
        profileMutableLiveData.setValue(profile);
    }
}
