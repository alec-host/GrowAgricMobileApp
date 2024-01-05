package com.farmerfirst.growagric.api.http;

import android.net.Uri;

import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.RetrofitClient;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageWithData {
    private RequestBody user_uuid;
    private RequestBody name;
    private RequestBody description;
    private RequestBody action;

    public UploadImageWithData(RequestBody user_uuid,RequestBody name, RequestBody description, RequestBody action){
        this.user_uuid = user_uuid;
        this.name = name;
        this.description = description;
        this.action = action;
    }

    public void HttpPost(File imageFile,ICustomResponseCallback iCustomResponseCallback){

        final IApiInterface iApiInterface = RetrofitClient.getApiService();

        RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/*"),imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",imageFile.getName(),imageRequestBody);

        iApiInterface.uploadImageFile(imagePart,user_uuid,name,description,action).enqueue(new Callback<ResponseBody>(){
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                iCustomResponseCallback.onFailure();
            }
        });
    }
    public File ReadFile(Uri path){
        File imageFile = new File(path.getPath());
        return imageFile;
    }
}
