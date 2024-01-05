package com.farmerfirst.growagric.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

import com.farmerfirst.growagric.api.callbacks.ICustomResponseCallback;
import com.farmerfirst.growagric.firebase.FireBaseHttpPost;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetworkUtils {

    private static Context context;
    private static NetworkUtils instance = new NetworkUtils();
    private boolean connected = false;

    public static NetworkUtils getInstance(@NonNull Context ctx){
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline(){
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting();
            return connected;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean pingUrl(final String uri) {
        try {
            final URL url = new URL(uri);
            final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setConnectTimeout(1000 * 10);
            urlConn.connect();
            if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }else{
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static OkHttpClient clientTimeout(){
        OkHttpClient innerClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .build();

        return innerClient;
    }
}