package com.housing.typeracer;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class MainApplication extends Application {
    private static MainApplication context;
    public static boolean mIsHost = false;

    public MainApplication() {
        context = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static Context getContext() {
        return context;
    }

    public static void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToast(int id) {
        Toast.makeText(context, context.getResources().getString(id), Toast.LENGTH_LONG).show();
    }

}
