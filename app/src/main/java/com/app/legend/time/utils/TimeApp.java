package com.app.legend.time.utils;

import android.app.Application;
import android.content.Context;

public class TimeApp extends Application {

    private static Context context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        SlideHelper.setApplication(this);
    }
}
