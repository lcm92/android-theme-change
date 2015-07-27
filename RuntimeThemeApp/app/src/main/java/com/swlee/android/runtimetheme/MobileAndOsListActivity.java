package com.swlee.android.runtimetheme;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by wonlees on 5/19/15.
 *
 * Shows how to use layout in another app.
 * In this example, another app(RuntimeThemeResource) has only resources such as images, string, color and layout.
 *
 * You will see the difference when we use activity_mobile_n_os_list in this app and
 * when we use activity_mobile_n_os_list in the RuntimeThemeResource app.
 *
 * Korean and Chines string resources are only in the RuntimeThemeResource app. So, you will see Korean and Chines after you install the RuntimeThemeResource app.
 *
 */
public class MobileAndOsListActivity extends ListActivity {
    private static final String LOG_TAG = MobileAndOsListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If there is no RuntimeThemeResource App installed, returned layout is the one that is in this app's resource.
        View layout = ResourceManager.findLayoutById(this, R.layout.activity_mobile_n_os_list);
        if (null != layout) {
            setContentView(layout);

            ImageButton imgBtn = (ImageButton) ResourceManager.findViewById(this, layout, R.id.btn_flight_info);
            if (null != imgBtn)
                imgBtn.setOnClickListener(mFlightInfoOnClick);

        } else { // just in case that there is no activity_mobile_n_os_list layout file in RuntimeThemeResource app!
            setContentView(R.layout.activity_mobile_n_os_list);

            ImageButton imgBtn = (ImageButton) findViewById(R.id.btn_flight_info);
            if (null != imgBtn)
                imgBtn.setOnClickListener(mFlightInfoOnClick);
        }

        ArrayList<HashMap<String, String>> list = buildData();
        RuntimeResourceListAdapter adapter = new RuntimeResourceListAdapter(MobileAndOsListActivity.this,
                R.layout.img_text_list_item_2, list);
        setListAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mResourceAppPkgNameReceiver, new IntentFilter(ResourceManager.ACTION_RELOAD_RESOURCE));

        String currentPkgName = ResourceManager.getResourceAppPackageName(this);
        if (!TextUtils.isEmpty(currentPkgName)) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            if (null != sharedPref) {
                String strPkgNameInSharedPref = sharedPref.getString(ResourceManager.SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME, getPackageName());
                if (!TextUtils.isEmpty(strPkgNameInSharedPref))
                    if (!strPkgNameInSharedPref.equals(currentPkgName))
                        exitAppToReloadResource();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mResourceAppPkgNameReceiver);
    }

    private ArrayList<HashMap<String, String>> buildData() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        list.add(makeData("Android", "Smart phone"));
        list.add(makeData("Windows Phone", "Smart phone"));
        list.add(makeData("iPhone", "Smart phone"));
        list.add(makeData("Blackberry", "Smart phone"));

        list.add(makeData("Unix", "Computer OS"));
        list.add(makeData("Linux", "Computer OS"));
        list.add(makeData("Free BSD", "Computer OS"));
        list.add(makeData("Windows7", "Computer OS"));
        list.add(makeData("Windows8", "Computer OS"));
        list.add(makeData("Windows10", "Computer OS"));
        list.add(makeData("OS X", "Computer OS"));

        return list;
    }

    private HashMap<String, String> makeData(String name, String type) {
        HashMap<String, String> item = new HashMap<>();
        item.put("name", name);
        item.put("type", type);
        return item;
    }

    private BroadcastReceiver mResourceAppPkgNameReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                String strPkgName = intent.getStringExtra(ResourceManager.EXTRA_RESOURCE_APK_PKG_NAME);
                Log.i(LOG_TAG, "mResourceAppPkgNameReceiver::onReceive() - Resource App Package Name: " + strPkgName);
                exitAppToReloadResource();
            }
        }
    };

    private void exitAppToReloadResource() {
        Toast.makeText(MobileAndOsListActivity.this, getString(R.string.exit_to_reload_resource_and_to_start_the_app), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MobileAndOsListActivity.this, ResourceUpdateActivity.class);
        startActivity(intent);
    }

    private View.OnClickListener mFlightInfoOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent ni = new Intent(MobileAndOsListActivity.this, InfoActivity.class);
            startActivity(ni);
        }
    };
}
