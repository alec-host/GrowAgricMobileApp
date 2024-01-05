package com.farmerfirst.growagric.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class LocalSharedPreferences {
    private static final int PRIVATE_MODE = Context.MODE_PRIVATE;
    private static final String PREF_NAME = "#farmerfirst";
    private final SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public LocalSharedPreferences(Context context){
        prefs = getMyPreferences(context);
        prefs = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = prefs.edit();
    }
    private static SharedPreferences getMyPreferences(@NonNull Context context){
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public boolean isLogin(){return prefs.getBoolean("isLogin",false);}
    public void setIsLogin(boolean isLogin){editor.putBoolean("isLogin", isLogin).commit();}
    public String getCountryCode(){return prefs.getString("countryCode","");}
    public void setCountryCode(String code){editor.putString("countryCode", code).commit();}
    public String getProfileInfo(){return prefs.getString("profileInfo",""); }
    public void setProfileInfo(String profileInfo){ editor.putString("profileInfo",profileInfo).commit();}
    public void deleteProfileInfo(){editor.remove("profileInfo").apply();}
    public void setUserToken(String token){editor.putString("userToken",token).commit();}
    public String getUserToken(){return prefs.getString("userToken","");}
    public void deleteUserToken(){editor.remove("userToken").apply();}
    public void setFarm(String farm){editor.putString("farm",farm).commit();}
    public String getFarm(){return prefs.getString("farm","");}
    public void setFinance(String finance){editor.putString("finance",finance).commit();}
    public String getFinance(){return prefs.getString("finance","");}
    public void setAccountValidationStatus(boolean isValid){editor.putBoolean("isValid",isValid).commit();}
    public boolean getAccountValidationStatus(){return prefs.getBoolean("isValid",false);}
    public void setPhoneNo(String phone){editor.putString("phoneNumber",phone).commit();}
    public String getPhoneNo(){return prefs.getString("phoneNumber","");}
    public void deletePhoneNo(){editor.remove("phoneNumber").apply();}
    public void setFarmerHashMapData(HashMap<String,String> searchData){editor.putString("searchFarmerHashMapData",searchData.toString()).commit();}
    public String getFarmerHashMapData(){return prefs.getString("searchFarmerHashMapData","");}
    public void deleteFarmerHashMapData(){editor.remove("searchFarmerHashMapData");}
    public String getChatHeadCounter(){return prefs.getString("chatHeadCount","0");}
    public void setChatHeadCounter(String counter){editor.putString("chatHeadCount",counter).commit();}
    public void setFcmToken(String token){editor.putString("fcm",token).commit();}
    public String getFcmToken(){return prefs.getString("fcm","");}
    public void deleteFcmToken(){editor.remove("fcm").apply();}
    public void setFarmInfo(String farmInfo){editor.putString("farmInfo",farmInfo).commit();}
    public String getFarmInfo(){return prefs.getString("farmInfo","");}
    public void deleteFarmInfo(){editor.remove("farmInfo").apply();}
    public void setFarmerInfo(String farmerInfo){editor.putString("farmerInfo",farmerInfo).commit();}
    public String getFarmerInfo(){return prefs.getString("farmerInfo","");}
    public void deleteFarmerInfo(){editor.putString("farmerInfo","").apply();}
    public void setTrackProfileInfoUpdate(int profileInfoUpdate){editor.putInt("profileInfoUpdate",profileInfoUpdate).commit();}
    public int getTrackProfileInfoUpdate(){return prefs.getInt("profileInfoUpdate",0);}
    public void deleteTrackProfileInfoUpdate(){editor.remove("profileInfoUpdate").apply();}
    public void setAvatar(Uri uri) {editor.putString("avatar",uri.toString()).commit();}
    public String getAvatar(){return prefs.getString("avatar","");}
    public void deleteAvatar(){editor.remove("avatar").apply();}
}
