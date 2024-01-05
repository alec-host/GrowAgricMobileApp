package com.farmerfirst.growagric.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> data = new MutableLiveData<>();

    public LiveData<String> getData(){
        return data;
    }
    public void updateData(String newData){
        //data.setValue(newData);
        data.setValue("hello worls");
    }
}