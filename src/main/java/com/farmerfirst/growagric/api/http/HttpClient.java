package com.farmerfirst.growagric.api.http;

import androidx.annotation.NonNull;

import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpClient {
    private static final IApiInterface iApiInterface = RetrofitClient.getApiService();

    //-.package record_keeping.
    public static void PostFarmRecord(JSONObject obj, ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.addFarmRecord(obj.toString()).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                try{
                    if(response.code() == 200 || response.code() == 201){
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.toString());
                        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXx record keep "+obj.toString());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call,Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }
    //-.package: invite.
    public static void PostInvite(JSONObject obj,ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.addInvite(obj.toString()).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                try{
                    if(response.code() == 200 || response.code() == 201){
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.get("message").toString());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call,Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }
    //-.package: profile.
    public static void ModifiyUser(String user_uuid, JSONObject obj, ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.modifyUser(user_uuid,obj.toString()).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.toString());
                    }
                }catch (Exception ex){
                    iCustomResponseCallback.onFailure();
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }
    //-.package: profile.
    public static void postHearAboutUsSource(JSONObject obj, ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.addHearAboutUsSource(obj.toString()).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response){
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        //JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess("success");
                    }
                }catch (Exception ex){
                    iCustomResponseCallback.onFailure();
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t){
                iCustomResponseCallback.onFailure();
                t.printStackTrace();
            }
        });
    }
    //-.splash.
    public static void getProfileStatus(String phoneNumber,ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.getProfileStatus(phoneNumber).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
                try{
                    if(response.code() == 200 || response.code() == 201){
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.get("message").toString());
                    }else{
                        iCustomResponseCallback.onFailure();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }
    //-.splash.
    public static void checkAccountVerifiedStatus(String phoneNumber, ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.getAccountVerificationState(phoneNumber).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                try{
                    if(response.code() == 200 || response.code() == 201){
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.get("message").toString());
                    }else{
                        iCustomResponseCallback.onFailure();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iCustomResponseCallback.onFailure();
                t.printStackTrace();
            }
        });
    }
    //-.login.
    public static void getUserProfile(String phoneNumber,ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.profile(phoneNumber).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.toString());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }
    //-.login.
    public static void authenticateUser(JSONObject obj,ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.login(obj.toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response) {
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        iCustomResponseCallback.onSuccess(responseBody);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }
    //-.otp verification.
    public static void verifyPhoneNumber(JSONObject obj,ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.verifyPhone(obj.toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if (response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.get("message").toString());
                    } else {
                        iCustomResponseCallback.onFailure();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iCustomResponseCallback.onFailure();
                t.printStackTrace();
            }
        });
    }
    //-.change password.
    public static void forgotPassword(JSONObject obj,ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.changePassword(obj.toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if (response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.get("message").toString());
                    } else {
                        iCustomResponseCallback.onFailure();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                iCustomResponseCallback.onFailure();
                t.printStackTrace();
            }
        });
    }
    //-.create users.
    public static void registerNewUser(JSONObject obj,ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.register(obj.toString()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        //-.store profile data.
                        //---prefs.setProfileInfo(obj.toString());
                        //---binding.buttonSubmit.hideLoading();
                        //-.navigation.
                        //--Intent intent = new Intent(RegisterActivity.this, VerifyActivity.class);
                        //--intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //--startActivity(intent);
                        //---finish();
                        iCustomResponseCallback.onSuccess(obj.toString());
                    }else{
                        iCustomResponseCallback.onFailure();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }

    public static void verificationCodeRequest(String phoneNumber,ICustomResponseCallback iCustomResponseCallback){
        iApiInterface.sendOTP(phoneNumber).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                try {
                    if (response.code() == 200 || response.code() == 201){
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);
                        iCustomResponseCallback.onSuccess(obj.get("message").toString());
                    }
                }catch (Exception ex){ex.printStackTrace();}
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                iCustomResponseCallback.onFailure();
                t.printStackTrace();
            }
        });
    }
}