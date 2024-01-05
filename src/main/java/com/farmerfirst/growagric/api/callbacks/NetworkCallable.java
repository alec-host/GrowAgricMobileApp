package com.farmerfirst.growagric.api.callbacks;

import android.content.Context;

import com.farmerfirst.growagric.api.IApiInterface;
import com.farmerfirst.growagric.utils.NetworkUtils;

import java.util.concurrent.Callable;

public class NetworkCallable implements Callable<Boolean> {
    private Context context;
    public NetworkCallable(Context context){
        this.context = context;
    }
    @Override
    public Boolean call() throws Exception{
        return NetworkUtils.getInstance(context).pingUrl(IApiInterface.BASE_URL+"api/v1/users/ping");
    }
}