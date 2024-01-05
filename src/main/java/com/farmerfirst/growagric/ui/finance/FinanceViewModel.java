package com.farmerfirst.growagric.ui.finance;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FinanceViewModel extends ViewModel {

    public MutableLiveData<String> FarmDetails = new MutableLiveData<>();
    public MutableLiveData<String> NumberOfBirdsToRaisedNow = new MutableLiveData<>();
    public MutableLiveData<String> AverageMortalityRate = new MutableLiveData<>();
    public MutableLiveData<String> LoanAmount = new MutableLiveData<>();
    public MutableLiveData<String> FinanceDate = new MutableLiveData<>();
    public MutableLiveData<String> TotalCostOfChicks = new MutableLiveData<>();
    public MutableLiveData<String> TotalCostOfFeeds = new MutableLiveData<>();
    public MutableLiveData<String> TotalBroodingCost = new MutableLiveData<>();
    public MutableLiveData<String> MedicineVaccineCost = new MutableLiveData<>();
    public MutableLiveData<String> ProjectSalePricePerChick = new MutableLiveData<>();
    public MutableLiveData<String> ChickSupplier = new MutableLiveData<>();
    public MutableLiveData<String> FeedSupplier = new MutableLiveData<>();

    public MutableLiveData<AddFinance> FinanceMutableLiveData;

    public MutableLiveData<AddFinance> getFinaceRequest() {
        if(FinanceMutableLiveData == null) {
            FinanceMutableLiveData = new MutableLiveData<>();
        }
        return FinanceMutableLiveData;
    }

    public void onClick(View view){
        AddFinance addFinance = new AddFinance(FarmDetails.getValue(),NumberOfBirdsToRaisedNow.getValue(),
                                               AverageMortalityRate.getValue(),LoanAmount.getValue(),
                                               FinanceDate.getValue(),TotalCostOfChicks.getValue(),TotalCostOfFeeds.getValue(),
                                               TotalBroodingCost.getValue(),MedicineVaccineCost.getValue(),ProjectSalePricePerChick.getValue(),
                                               ChickSupplier.getValue(),FeedSupplier.getValue());

        FinanceMutableLiveData.setValue(addFinance);
    }

    public String costEstimate(){
        return (TotalCostOfChicks.getValue()+TotalCostOfFeeds+TotalBroodingCost+MedicineVaccineCost);
    }
}
