package com.housing.typeracer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class MainApplication extends Application {
    private static MainApplication context;
    public static boolean mIsHost = false;
    public static boolean profileSaved = false;
    public static String prof_key = "prof_saved";

    private static SharedPreferences sharedPreferences;

    public static Map<String, String> USER_NAME;
    public static Map<String, String> USER_REMOTE_ENDPOINT;
    public static Map<String, Integer> USER_SCORE;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        resetGameUsers();
    }

    public static void resetGameUsers() {
        USER_NAME = new HashMap<>();
        USER_REMOTE_ENDPOINT = new HashMap<>();
        USER_SCORE = new HashMap<>();
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
