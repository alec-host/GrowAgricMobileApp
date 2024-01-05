package com.farmerfirst.growagric.ui.record_keeping.data;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientData {
    public static JSONObject AddFarmRecordPayload(String farm_uuid,String farmer_uuid,double cr,double dr,double running_balance,String description,String record_type,String notes,String entry_date){
        JSONObject object = new JSONObject();
        try {
            object.put("head", "mobi");
            object.put("farm_uuid", farm_uuid);
            object.put("farmer_uuid", farmer_uuid);
            object.put("cr", cr);
            object.put("dr", dr);
            object.put("running_balance", running_balance);
            object.put("description", description);
            object.put("record_type", record_type);
            object.put("notes", notes);
            object.put("entry_date", entry_date);

            return object;
        }catch (JSONException e){
            return null;
        }
    }
}
