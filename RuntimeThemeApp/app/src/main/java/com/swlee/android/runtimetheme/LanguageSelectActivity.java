package com.swlee.android.runtimetheme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


/**
 * Created by wonlees on 5/26/15.
 *
 * Shows how to use another app's resource without ResourceManager.
 * In this example, another app(RuntimeThemeResource) has only resources such as images, string, color and layout.
 *
 * Korean and Chines string resources are only in the RuntimeThemeResource app. So, you will see Korean and Chines after you install the RuntimeThemeResource app.
 *
 */
public class LanguageSelectActivity extends Activity {
    private static final String LOG_TAG = LanguageSelectActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String strResourcePkgName = ResourceManager.getResourceAppPackageName(this);
        Context resourceApkContext = ResourceManager.getResourceAppContext(this);
        Resources resources = ResourceManager.getResourceAppResources(this);

        if (null != resourceApkContext && null != resources) {
            // Sets Activity background image
            int otherAppResourceId = resources.getIdentifier("theme_app_bg", "drawable", strResourcePkgName);
            if (otherAppResourceId != 0) {
                Drawable drawable = resources.getDrawable(otherAppResourceId);
                if (null != drawable) {
                    View rootView = findViewById(R.id.activity_root);
                    if (null != rootView)
                        rootView.setBackgroundDrawable(drawable);
                }
            }

            // Sets TextView text color
            TextView tvSelectLanguage = (TextView) findViewById(R.id.tv_select_language);
            otherAppResourceId = resources.getIdentifier("theme_primary_text_color", "color", strResourcePkgName);
            if (otherAppResourceId != 0) {
                int iColor = resources.getColor(otherAppResourceId);
                if (null != tvSelectLanguage)
                    tvSelectLanguage.setTextColor(iColor);
            }

            // Sets selectors into the button Korean
            Button btnKor = (Button) findViewById(R.id.btn_kor);
            otherAppResourceId = resources.getIdentifier("theme_btn_default_bg_state", "drawable", strResourcePkgName);
            if (otherAppResourceId != 0) {
                Drawable drawable = resources.getDrawable(otherAppResourceId);
                if (null != drawable && null != btnKor)
                    btnKor.setBackgroundDrawable(drawable);
            }
            otherAppResourceId = resources.getIdentifier("theme_btn_default_text_state", "color", strResourcePkgName);
            if (otherAppResourceId != 0) {
                ColorStateList csl = resources.getColorStateList(otherAppResourceId);
                if (null != csl && null != btnKor)
                    btnKor.setTextColor(csl);
            }

            // Sets selectors into the button Chinese
            Button btnChinese = (Button) findViewById(R.id.btn_chinese);
            otherAppResourceId = resources.getIdentifier("theme_btn_default_bg_state", "drawable", strResourcePkgName);
            if (otherAppResourceId != 0) {
                Drawable drawable = resources.getDrawable(otherAppResourceId);
                if (null != drawable && null != btnChinese)
                    btnChinese.setBackgroundDrawable(drawable);
            }
            otherAppResourceId = resources.getIdentifier("theme_btn_default_text_state", "color", strResourcePkgName);
            if (otherAppResourceId != 0) {
                ColorStateList csl = resources.getColorStateList(otherAppResourceId);
                if (null != csl && null != btnChinese)
                    btnChinese.setTextColor(csl);
            }

            // Sets selectors into the button English
            Button btnEng = (Button) findViewById(R.id.btn_eng);
            otherAppResourceId = resources.getIdentifier("theme_btn_default_bg_state", "drawable", strResourcePkgName);
            if (otherAppResourceId != 0) {
                Drawable drawable = resources.getDrawable(otherAppResourceId);
                if (null != drawable && null != btnEng)
                    btnEng.setBackgroundDrawable(drawable);
            }
            otherAppResourceId = resources.getIdentifier("theme_btn_default_text_state", "color", strResourcePkgName);
            if (otherAppResourceId != 0) {
                ColorStateList csl = resources.getColorStateList(otherAppResourceId);
                if (null != csl && null != btnEng)
                    btnEng.setTextColor(csl);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_language_select, menu);
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

    public void onClickHandler(View v) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Resources resources = ResourceManager.getResourceAppResources(this);

        if (null != v) {
            switch (v.getId()) {
                case R.id.btn_kor:
                    if (null != resources) {
                        Configuration config = resources.getConfiguration();
                        config.locale = Locale.KOREA;
                        resources.updateConfiguration(config, null);
                    }

                    if (null != sharedPref) {
                        SharedPreferences.Editor ed = sharedPref.edit();
                        ed.putString(ResourceManager.SHARED_PREF_KEY_USER_LANGUAGE, ResourceManager.KOREAN);
                        boolean result = ed.commit();
                        Log.i(LOG_TAG, "onClickHandler() - Saving Language KO: " + result);
                    }
                    break;

                case R.id.btn_chinese:
                    if (null != resources) {
                        Configuration config = resources.getConfiguration();
                        config.locale = Locale.CHINESE;
                        resources.updateConfiguration(config, null);
                    }

                    if (null != sharedPref) {
                        SharedPreferences.Editor ed = sharedPref.edit();
                        ed.putString(ResourceManager.SHARED_PREF_KEY_USER_LANGUAGE, ResourceManager.CHINESE);
                        boolean result = ed.commit();
                        Log.i(LOG_TAG, "onClickHandler() - Saving Language CHINESE: " + result);
                    }
                    break;

                case R.id.btn_eng:
                    if (null != resources) {
                        Configuration config = resources.getConfiguration();
                        config.locale = Locale.ENGLISH;
                        resources.updateConfiguration(config, null);
                    }

                    if (null != sharedPref) {
                        SharedPreferences.Editor ed = sharedPref.edit();
                        ed.putString(ResourceManager.SHARED_PREF_KEY_USER_LANGUAGE, "en");
                        boolean result = ed.commit();
                        Log.i(LOG_TAG, "onClickHandler() - Saving Language EN: " + result);
                    }
                    break;
            }
        }

        Intent intent = new Intent(this, MobileAndOsListActivity.class);
        startActivity(intent);
        //finish();
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
        Toast.makeText(LanguageSelectActivity.this, getString(R.string.exit_to_reload_resource_and_to_start_the_app), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LanguageSelectActivity.this, ResourceUpdateActivity.class);
        startActivity(intent);
    }
}
