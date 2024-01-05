package com.farmerfirst.growagric.ui.login.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject signInPayload(String phoneNumber,String password){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("phone_number", phoneNumber);
            object.put("password", password);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
}
