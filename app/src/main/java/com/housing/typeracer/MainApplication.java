package com.housing.typeracer;

import android.app.Application;
import android.content.Context;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class MainApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }


    public static Context getContext() {
        return context;
    }
}
