package com.farmerfirst.growagric.ui.farm.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject AddFarmPayload(String phoneNumber, String farmerUUID, String farmedItem, String chickHouseCapacity, String county, String subCounty, String ward, String otherFarmedItems,String noOfYearsFarmingSelectedItem, String noOfEmployees, String hasInsurance, String insurerName, String challenges, String otherChallenges){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("phone_number", phoneNumber);
            object.put("farmer_uuid", farmerUUID);
            object.put("item_farmed", farmedItem);
            object.put("county", county);
            object.put("sub_county", subCounty);
            object.put("ward", ward);
            object.put("otherFarmedOther",otherFarmedItems);
            object.put("bird_house_capacity", chickHouseCapacity);
            object.put("number_of_years_farming", noOfYearsFarmingSelectedItem);
            object.put("number_of_employees", noOfEmployees);
            object.put("is_insured", hasInsurance);
            object.put("insurer", insurerName);
            object.put("challenges_faced", challenges);
            object.put("other_challenges", otherChallenges);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
}
