package com.farmerfirst.growagric.ui.farm;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FarmViewModel extends ViewModel{

    public MutableLiveData<String> FarmedItem = new MutableLiveData<>();
    public MutableLiveData<String> ChickHouseCapacity = new MutableLiveData<>();
    public MutableLiveData<String> County = new MutableLiveData<>();
    public MutableLiveData<String> SubCounty = new MutableLiveData<>();
    public MutableLiveData<String> Ward = new MutableLiveData<>();
    public MutableLiveData<String> NumberOfYearsFarmingSelectedItem = new MutableLiveData<>();
    public MutableLiveData<String> NumberOfEmployees = new MutableLiveData<>();
    public MutableLiveData<String> IsFarmInsured = new MutableLiveData<>();
    public MutableLiveData<String> InsurerName = new MutableLiveData<>();
    public MutableLiveData<String> ListOfChallenges = new MutableLiveData<>();
    public MutableLiveData<String> OtherChallenges = new MutableLiveData<>();
    public MutableLiveData<String> NumberOfChickhouse = new MutableLiveData<>();

    private MutableLiveData<AddFarm> FarmMutableLiveData;

    public MutableLiveData<AddFarm> getCreatedFarm() {
        if(FarmMutableLiveData == null){
            FarmMutableLiveData = new MutableLiveData<>();
        }
        return FarmMutableLiveData;
    }

    public void onClick(View view){
        AddFarm addFarm = new AddFarm(FarmedItem.getValue(),ChickHouseCapacity.getValue(),County.getValue(),SubCounty.getValue(),Ward.getValue(),
                NumberOfYearsFarmingSelectedItem.getValue(),NumberOfEmployees.getValue(),IsFarmInsured.getValue(),
                InsurerName.getValue(),ListOfChallenges.getValue(),OtherChallenges.getValue(),NumberOfChickhouse.getValue());

        FarmMutableLiveData.setValue(addFarm);
    }
}
