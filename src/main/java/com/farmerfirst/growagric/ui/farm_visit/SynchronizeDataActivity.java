package com.farmerfirst.growagric.ui.farm_visit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.farmerfirst.growagric.R;
import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.databinding.ActivitySynchronizeDataBinding;
import com.farmerfirst.growagric.datastore.LocalSharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SynchronizeDataActivity extends AppCompatActivity{
    private ActivitySynchronizeDataBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_synchronize_data);

        binding.buttonSyncData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                LocalSharedPreferences prefs = new LocalSharedPreferences(SynchronizeDataActivity.this);

                syncFarmerInfo(new ICustomResponseCallback() {
                    @Override
                    public void onSuccess(String value) {
                        if(prefs.getFarmerInfo().equals("")){
                            prefs.setFarmerInfo(value);
                        }else{
                            prefs.deleteProfileInfo();
                            prefs.setFarmerInfo(value);
                        }
                        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX "+prefs.getProfileInfo());
                        /*
                        JSONArray array = new JSONArray(responseBody);
                        ArrayList<String> farmerArrayList = new ArrayList<>();

                        for(int j=0;j<=array.length();j++){
                            farmerArrayList.add(array.toString(j));
                        }
                         */
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }
        });
    }

    private void syncFarmerInfo(ICustomResponseCallback iCustomResponseCallback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IApiInterface.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IApiInterface iApiInterface = retrofit.create(IApiInterface.class);

        int page = 20;
        int size = 10000;

        iApiInterface.getFarmerList(page,size).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
                try {
                    if (response.code() == 200 || response.code() == 201) {
                        String responseBody = response.body().string();
                        JSONObject obj = new JSONObject(responseBody);

                        /*
                        JSONArray array = new JSONArray(responseBody);
                        ArrayList<String> farmerArrayList = new ArrayList<>();

                        for(int j=0;j<=array.length();j++){
                            farmerArrayList.add(array.toString(j));
                        }
                        */

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
    private void syncFarmInfo(ICustomResponseCallback iCustomResponseCallback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IApiInterface.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IApiInterface iApiInterface = retrofit.create(IApiInterface.class);

        int page = 20;
        int size = 10000;

        iApiInterface.getFarmList(page,size).enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response){
                try {
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
}