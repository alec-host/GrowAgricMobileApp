package com.farmerfirst.growagric.datastore;

import com.farmerfirst.growagric.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LocalData {
    public static String GetUserUUID(LocalSharedPreferences prefs){
        String userUUID = "";
        String profile = prefs.getProfileInfo();
        try {
            JSONObject obj = new JSONObject(profile);
            userUUID = obj.getJSONObject("message").get("farmer_uuid").toString();
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return userUUID;
    }
    public static String GetUserPhoneNumber(LocalSharedPreferences prefs){
        String phoneNumber = "";
        String profile = prefs.getProfileInfo();
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx "+profile);
        if(profile!="") {
            try {
                JSONObject obj = new JSONObject(profile);
                phoneNumber = obj.getJSONObject("message").get("phone_number").toString();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return phoneNumber;
    }
    public static String GetUserFirstName(LocalSharedPreferences prefs){
        String firstName = "";
        String profile = prefs.getProfileInfo();
        try {
            JSONObject obj = new JSONObject(profile);
            firstName = Utils.capitalizeFirstLetter(obj.getJSONObject("message").get("first_name").toString());
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return firstName;
    }
}
