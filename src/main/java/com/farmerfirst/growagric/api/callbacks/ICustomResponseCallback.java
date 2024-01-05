package com.farmerfirst.growagric.api.callbacks;


public interface ICustomResponseCallback {
    void onSuccess(String value);
    void onFailure();
}
