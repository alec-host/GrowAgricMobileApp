package com.farmerfirst.growagric.firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class FireBaseJson {
    public static JSONObject data(String firebaseToken){
        if(firebaseToken != ""){
            JSONObject object = new JSONObject();
            try {
                object.put("head","mobi");
                object.put("push_notification_token",firebaseToken);
                System.out.println(object.toString());
                return object;
            } catch (JSONException e){
                return null;
            }
        }else{
            return null;
        }
    }
}
