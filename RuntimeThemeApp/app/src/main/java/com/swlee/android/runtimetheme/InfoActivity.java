package com.swlee.android.runtimetheme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * Created by wonlees on 5/20/15.
 *
 * Shows how to use ResourceManager to use another app's resources when we use our own layout file in this app.
 * In this example, another app(RuntimeThemeResource) has only resources such as images, string, color and layout.
 *
 * Korean and Chinese string resources are only in the RuntimeThemeResource app. So, you will see Korean and Chines after you install the RuntimeThemeResource app.
 *
 */
public class InfoActivity extends Activity {
    private static final String LOG_TAG = InfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Set background image from RuntimeThemeResource when there is RuntimeThemeResource app installed.
        ResourceManager.findViewById(this, R.id.ll_activity_info_root, new int[]{R.drawable.theme_app_bg});

        // Set text and text color from RuntimeThemeResource when there is RuntimeThemeResource app installed.
        ResourceManager.findViewById(this, R.id.tv_info_title,
                new int[]{R.string.theme_information_activity_title, R.color.theme_primary_text_color});

        // Set button background selector, text color state list and string from RuntimeThemeResource when there is RuntimeThemeResource app installed.
        Button btn = (Button) ResourceManager.findViewById(this, R.id.btn_info,
                new int[]{R.drawable.theme_btn_default_bg_state, R.color.theme_btn_default_text_state, R.string.theme_info});
        if (null != btn) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(InfoActivity.this, ResourceManager.getString(InfoActivity.this, R.string.theme_info), Toast.LENGTH_SHORT).show();
                }
            });
        }

        ToggleButton btnCallAttendant = (ToggleButton) ResourceManager.findViewById(this, R.id.toggle_btn_call_front,
                new int[]{R.drawable.theme_btn_default_bg_state, R.color.theme_btn_default_text_state});
        if (null != btn) {
            btnCallAttendant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(InfoActivity.this, isChecked ? "ON" : "OFF", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        Toast.makeText(InfoActivity.this, getString(R.string.exit_to_reload_resource_and_to_start_the_app), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(InfoActivity.this, ResourceUpdateActivity.class);
        startActivity(intent);
    }
}
