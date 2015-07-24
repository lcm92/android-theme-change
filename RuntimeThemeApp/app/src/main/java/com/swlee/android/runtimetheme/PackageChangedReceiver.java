package com.swlee.android.runtimetheme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

public class PackageChangedReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = PackageChangedReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (null != context && null != intent) {
            final String action = intent.getAction();
            final String packageName = intent.getData().getSchemeSpecificPart();

            if (TextUtils.isEmpty(action) || TextUtils.isEmpty(packageName)) {
                // they sent us a bad intent
                Log.e(LOG_TAG, "PackageChangedReceiver::onReceive() - action and/or package name are empty!");
                return;
            }

            Log.i(LOG_TAG, "PackageChangedReceiver::onReceive() - Package Name: " + packageName);

            if (packageName.contains(context.getPackageName())
                    && !packageName.equals(context.getPackageName())) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                if (null != sharedPref) {
                    if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                        //boolean result = sp.edit().clear().commit();
                        boolean result = sharedPref.edit().remove(ResourceManager.SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME).commit();
                        Log.i(LOG_TAG, "PackageChangedReceiver::onReceive() - Deleting Resource Apk Package Name: " + result);
                    } else {
                        String strPkgNameInSharedPref = sharedPref.getString(ResourceManager.SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME, context.getPackageName());
                        if (!TextUtils.isEmpty(strPkgNameInSharedPref)) {
                            if (!strPkgNameInSharedPref.equals(packageName)) {
                                SharedPreferences.Editor ed = sharedPref.edit();
                                ed.putString(ResourceManager.SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME, packageName);
                                boolean result = ed.commit();
                                Log.i(LOG_TAG, "PackageChangedReceiver::onReceive() - Saving Resource Apk Package Name: " + result);
                            }
                        }
                    }
                }

                String strNewPkgName;
                if (action.equals(Intent.ACTION_PACKAGE_REMOVED))
                    strNewPkgName = context.getPackageName();
                else
                    strNewPkgName = packageName;

                ResourceManager.reloadResourcesByPkgName(context, strNewPkgName);

                Intent reloadResource = new Intent(ResourceManager.ACTION_RELOAD_RESOURCE);
                //reloadResource.putExtra(ResourceManager.EXTRA_RESOURCE_APP_PKG_NAME, packageName);
                reloadResource.putExtra(ResourceManager.EXTRA_RESOURCE_APK_PKG_NAME, strNewPkgName);
                context.sendBroadcast(reloadResource);
            }
        }
    }
}
