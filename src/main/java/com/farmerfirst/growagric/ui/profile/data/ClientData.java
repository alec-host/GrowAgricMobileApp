package com.farmerfirst.growagric.ui.profile.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject ModifyUserPayload(String IDNumber,String email,String gender,String age,String maritalStatus,String levelOfEducation,String homeCounty){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("id_number", IDNumber);
            object.put("email", email);
            object.put("gender", gender);
            object.put("age",age);
            object.put("is_married", maritalStatus);
            object.put("level_of_education",levelOfEducation);
            object.put("home_county", homeCounty);
            return object;
        }catch (JSONException e){
            return null;
        }
    }

    public static JSONObject AddAboutUsPlatformPayload(String user_uuid, String applicantName,String phoneNumber,String platform,String dateCreated){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("farmer_uuid",user_uuid);
            object.put("full_name",applicantName);
            object.put("phone_number",phoneNumber);
            object.put("platform",platform);
            object.put("date_created",dateCreated);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
}