package com.farmerfirst.growagric;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

import java.time.Instant;

public class App extends Application {
    private static App instant;
    public static App getInstance() {
        return instant;
    }
    public static Context getContext() {
        return instant;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        instant = this;
        FirebaseApp.initializeApp(instant);
    }
}
