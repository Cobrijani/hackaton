package com.example.stanko.myapplication;

import android.app.Application;

import com.gimbal.android.Gimbal;
import com.gimbal.logging.GimbalLogConfig;
import com.gimbal.logging.GimbalLogLevel;

import org.androidannotations.annotations.EApplication;

@EApplication
public class BeaconApplication extends Application {

    private static final String GIMBAL_API_KEY = "d02c6c2f-8975-4cf2-9cc6-d28d9f6c8648";

    @Override
    public void onCreate() {
        super.onCreate();

        Gimbal.setApiKey(this, GIMBAL_API_KEY);
        GimbalLogConfig.setLogLevel(GimbalLogLevel.INFO);
        GimbalLogConfig.enableFileLogging(this);
    }

}
