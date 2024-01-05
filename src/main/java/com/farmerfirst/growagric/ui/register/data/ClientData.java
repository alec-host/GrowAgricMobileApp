package com.farmerfirst.growagric.ui.register.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject signUpPayload(String firstName, String lastName, String phoneNumber, String password, String confirmPassword){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("first_name", firstName);
            object.put("last_name", lastName);
            object.put("phone_number", phoneNumber);
            object.put("password", password);
            object.put("confirm_password", confirmPassword);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
}
