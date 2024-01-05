package com.farmerfirst.growagric.api.callbacks;

import okhttp3.ResponseBody;

public interface ICustomResponseBodyCallback {
    void onSuccess(ResponseBody value);
    void onFailure();
}
