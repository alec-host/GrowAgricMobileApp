package com.farmerfirst.growagric.ui.invite.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject InvitePayload(String invitee_name,String invitee_phone_number,String farmer_uuid){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("invitee_name", invitee_name);
            object.put("invitee_phone_number", invitee_phone_number);
            object.put("farmer_uuid",farmer_uuid);
            return object;
        }catch (JSONException e){
            return null;
        }
    }
}
