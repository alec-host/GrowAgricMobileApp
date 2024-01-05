package com.farmerfirst.growagric.ui.otp.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject verifyPhonePayload(String phoneNumber,String code){
        JSONObject object = new JSONObject();
        try {
            object.put("head","mobi");
            object.put("phone_number",phoneNumber);
            object.put("otp",code);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
}