package com.swlee.android.runtimetheme;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

/**
 * Created by wonlees on 5/20/15.
 */
public class RuntimeThemeExample extends Application {
    private static final String LOG_TAG = RuntimeThemeExample.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LOG_TAG, "onCreate()...");

        ResourceManager.initResources(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(LOG_TAG, "onTerminate()...");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(LOG_TAG, "onLowMemory()...");
    }
}
