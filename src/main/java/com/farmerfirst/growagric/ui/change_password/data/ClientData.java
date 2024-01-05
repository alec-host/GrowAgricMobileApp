package com.farmerfirst.growagric.ui.change_password.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject changePasswordPayload(String user_uuid,String password,String confirmPassword,String code){
        JSONObject object = new JSONObject();
        try {
            object.put("head","mobi");
            object.put("user_uuid",user_uuid);
            object.put("password",password);
            object.put("confirm_password",confirmPassword);
            object.put("otp",code);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
}
