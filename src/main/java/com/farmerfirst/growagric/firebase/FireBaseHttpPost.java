package com.farmerfirst.growagric.firebase;

import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FireBaseHttpPost{
    public void firebaseToken(String userUUID,JSONObject obj,ICustomResponseCallback iCustomResponseCallback){
        final IApiInterface iApiInterface = RetrofitClient.getApiService();
        iApiInterface.setFcm(userUUID,obj.toString()).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
                try {
                    if (response.code() == 200) {
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
            }
        });
    }
}
