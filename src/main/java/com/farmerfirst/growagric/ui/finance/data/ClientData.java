package com.farmerfirst.growagric.ui.finance.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject AddFinancePayload(String applicantName, String phoneNumber, String farmUUID, String loanAmount, String noOfChickRaisedNow, String mortalityRate, String chickCost, String feedCost, String broodingCost, String medicineVaccineCost, String projectedChickSalePrice, String chickSupplier, String feedSupplier){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("applicant_name", applicantName);
            object.put("phone_number", phoneNumber);
            object.put("farmer_uuid", farmUUID);
            object.put("loan_amount", loanAmount);
            object.put("number_of_chicks_raised_now",noOfChickRaisedNow);
            object.put("mortality_rate",mortalityRate);
            object.put("chick_cost", chickCost);
            object.put("feed_cost", feedCost);
            object.put("brooding_cost", broodingCost);
            object.put("vaccine_medicine_cost", medicineVaccineCost);
            object.put("projected_sales_price_per_chick", projectedChickSalePrice);
            object.put("chick_supplier",chickSupplier);
            object.put("feed_supplier",feedSupplier);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
}
