package com.housing.typeracer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class MainApplication extends Application {
    private static MainApplication context;
    public static boolean mIsHost = false;

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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

    public static void putInSharedPref(String key, Object obj) {
        if (obj instanceof Boolean) {
            sharedPreferences.edit().putBoolean(key, (Boolean) obj).apply();
        } else if (obj instanceof String) {
            sharedPreferences.edit().putString(key, (String) obj).apply();
        } else if (obj instanceof Integer) {
            sharedPreferences.edit().putInt(key, (Integer) obj).apply();
        } else if (obj instanceof Float) {
            sharedPreferences.edit().putFloat(key, (Float) obj).apply();
        } else if (obj instanceof Long) {
            sharedPreferences.edit().putLong(key, (Long) obj).apply();
        } else {
            throw new TypeNotPresentException("type not found", new Exception());
        }
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

}
