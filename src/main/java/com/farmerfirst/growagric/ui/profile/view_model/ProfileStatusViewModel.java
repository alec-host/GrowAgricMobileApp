package com.farmerfirst.growagric.ui.profile.view_model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileStatusViewModel extends ViewModel {
    private MutableLiveData<String> data = new MutableLiveData<>();
    public LiveData<String> getData(){
        return data;
    }
    public void updateData(String newData){
        //data.setValue(newData);
        data.setValue("hello worls");
    }
}
