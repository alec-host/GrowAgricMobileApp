package com.farmerfirst.growagric.ui.finance;

public class AddFinance {
    //String strFarmUUID;
    String strFarmDetails;
    String strNumberOfBirdsToRaisedNow;
    String strAverageMortalityRate;
    String strLoanAmount;
    String strFinanceDate;
    String strTotalCostOfChicks;
    String strTotalCostOfFeeds;
    String strTotalBroodingCost;
    String strMedicineVaccineCost;
    String strProjectSalePricePerChick;
    String strChickSupplier;
    String strFeedSupplier;

    public AddFinance(String FarmDetails,String NumberOfBirdsToRaisedNow,String AverageMortalityRate,String LoanAmount,String FinanceDate,String TotalCostOfChicks,String TotalCostOfFeeds,String TotalBroodingCost,String MedicineVaccineCost,String ProjectSalePricePerChick,String ChickSupplier,String FeedSupplier){
        strFarmDetails = FarmDetails;
        strNumberOfBirdsToRaisedNow = NumberOfBirdsToRaisedNow;
        strAverageMortalityRate = AverageMortalityRate;
        strLoanAmount = LoanAmount;
        strFinanceDate = FinanceDate;
        strTotalCostOfChicks = TotalCostOfChicks;
        strTotalCostOfFeeds = TotalCostOfFeeds;
        strTotalBroodingCost = TotalBroodingCost;
        strMedicineVaccineCost = MedicineVaccineCost;
        strProjectSalePricePerChick = ProjectSalePricePerChick;
        strChickSupplier = ChickSupplier;
        strFeedSupplier = FeedSupplier;
    }

    public String getStrFarmDetails() {
        return strFarmDetails;
    }

    public String getStrNumberOfBirdsToRaisedNow() {
        return strNumberOfBirdsToRaisedNow;
    }

    public String getStrAverageMortalityRate() {
        return strAverageMortalityRate;
    }

    public String getStrLoanAmount() {
        return strLoanAmount;
    }

    public String getStrFinanceDate() {
        return strFinanceDate;
    }

    public String getStrTotalCostOfChicks() {
        return strTotalCostOfChicks;
    }

    public String getStrTotalCostOfFeeds() {
        return strTotalCostOfFeeds;
    }

    public String getStrTotalBroodingCost() {
        return strTotalBroodingCost;
    }

    public String getStrMedicineVaccineCost() {
        return strMedicineVaccineCost;
    }

    public String strProjectSalePricePerChick() {
        return strProjectSalePricePerChick();
    }

    public String getStrChickSupplier() {
        return strChickSupplier;
    }

    public String getStrFeedSupplier() {
        return strFeedSupplier;
    }
}


