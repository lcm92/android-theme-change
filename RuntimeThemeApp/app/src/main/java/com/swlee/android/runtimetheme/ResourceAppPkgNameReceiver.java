package com.swlee.android.runtimetheme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by wonlees on 5/20/15.
 */
public class ResourceAppPkgNameReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = ResourceAppPkgNameReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != context && null != intent) {
            String strNewPkgName = intent.getStringExtra(ResourceManager.EXTRA_RESOURCE_APK_PKG_NAME);
            Log.i(LOG_TAG, "ResourceAppPkgNameReceiver::onReceive() - Resource App Package Name: " + strNewPkgName);

            if (!TextUtils.isEmpty(strNewPkgName)) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                if (null != sharedPref) {
                    String strPkgNameInSharedPref = sharedPref.getString(ResourceManager.SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME, context.getPackageName());
                    if (!TextUtils.isEmpty(strPkgNameInSharedPref))
                        if (!strPkgNameInSharedPref.equals(strNewPkgName)) {
                            SharedPreferences.Editor ed = sharedPref.edit();
                            ed.putString(ResourceManager.SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME, strNewPkgName);
                            boolean result = ed.commit();
                            Log.i(LOG_TAG, "ResourceAppPkgNameReceiver::onReceive() - Saving Resource App Package Name: " + result);

                            // Get resource from Resource APK
                            //RuntimeThemeExample.reloadResourcesByPkgName(context, strNewPkgName);

                            Intent reloadResource = new Intent(ResourceManager.ACTION_RELOAD_RESOURCE);
                            reloadResource.putExtra(ResourceManager.EXTRA_RESOURCE_APK_PKG_NAME, strNewPkgName);
                            context.sendBroadcast(reloadResource);
                        }
                }
            }
        }
    }
}
