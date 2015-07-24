package com.swlee.android.runtimetheme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Locale;


/**
 * Created by wonlees on 7/7/15.
 */
public class ResourceManager {
    public static final String ACTION_NEW_RESOURCE_APK = "com.swlee.android.action.NEW_RESOURCE_APK";
    public static final String ACTION_RELOAD_RESOURCE = "com.swlee.android.action.RELOAD_RESOURCE";
    public static final String EXTRA_RESOURCE_APK_PKG_NAME = "com.swlee.android.extra.RESOURCE_APK_PKG_NAME";
    public static final String SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME = "_resource_app_pkg_name";
    public static final String SHARED_PREF_KEY_USER_LANGUAGE = "_user_language";

    public static final String KOREAN = "ko";
    public static final String CHINESE = "zh";

    private static final String LOG_TAG = ResourceManager.class.getSimpleName();

    private static final String NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT = "context is null.";
    private static final String NULL_POINTER_EXCEPTION_DESCRIPTION_PKG_NAME = "package name is null.";

    private static String sResourceAppPackageName;
    private static Context sResourceAppContext;
    private static Resources sResources;

    public static void initResources(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        if (null != sharedPref)
            sResourceAppPackageName = sharedPref.getString(ResourceManager.SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME, context.getPackageName());
        else
            sResourceAppPackageName = context.getPackageName();

        if (!TextUtils.isEmpty(sResourceAppPackageName)) {
            try {
                // getResourcesForApplication
                sResourceAppContext = context.createPackageContext(sResourceAppPackageName, 0);
            } catch (Exception e) {
                e.printStackTrace();
                sResourceAppContext = context;
            }

            if (null != sResourceAppContext) {
                try {
                    sResources = sResourceAppContext.getResources();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sResources = context.getResources();
                }
            }

            if (null != sResources) {
                // Sets language
                String savedLanguage = "unknown";

                if (null != sharedPref)
                    savedLanguage = sharedPref.getString(ResourceManager.SHARED_PREF_KEY_USER_LANGUAGE, "unknown");

                Log.i(LOG_TAG, "onCreate() - Saved Language: " + savedLanguage);

                if (!TextUtils.isEmpty(savedLanguage)) {
                    Configuration config = sResources.getConfiguration();
                    if (null != config) {
                        if (savedLanguage.equals(ResourceManager.KOREAN))
                            config.locale = Locale.KOREA;
                        else if (savedLanguage.equals(ResourceManager.CHINESE))
                            config.locale = Locale.CHINESE;
                        else
                            config.locale = Locale.ENGLISH;

                        sResources.updateConfiguration(config, null);
                    }
                }
            }
        }
    }

    /**
     * @param context
     * @return resource package name
     */
    public static String getResourceAppPackageName(final Context context) {
        if (null == context)
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT);

        if (TextUtils.isEmpty(sResourceAppPackageName)) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            if (null != sharedPref)
                sResourceAppPackageName = sharedPref.getString(SHARED_PREF_KEY_RESOURCE_APK_PKG_NAME, context.getPackageName());
            else
                sResourceAppPackageName = context.getPackageName();
        }

        return sResourceAppPackageName;
    }

    public static Context getResourceAppContext(final Context context) {
        if (null == context)
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT);

        if (null == sResourceAppContext) {
            try {
                // getResourcesForApplication
                sResourceAppContext = context.getApplicationContext().createPackageContext(getResourceAppPackageName(context), 0);
            } catch (Exception e) {
                e.printStackTrace();
                sResourceAppPackageName = context.getPackageName();
                return sResourceAppContext = context;
            }
        }

        return sResourceAppContext;
    }

    public static Resources getResourceAppResources(final Context context) {
        if (null == context)
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT);

        if (null == sResources) {
            Context resourceAppContext = getResourceAppContext(context);
            if (null != resourceAppContext)
                sResources = resourceAppContext.getResources();
            else
                sResources = context.getResources();
        }

        return sResources;
    }

    public static boolean reloadResources(Context context, String strNewResPkgName) {
        if (null == context)
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT);

        if (TextUtils.isEmpty(strNewResPkgName))
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_PKG_NAME);

        try {
            sResourceAppContext = context.getApplicationContext().createPackageContext(strNewResPkgName, 0);
        } catch (Exception e) {
            e.printStackTrace();
            sResourceAppPackageName = context.getPackageName();
            sResourceAppContext = context;
            sResources = context.getResources();
            return false;
        }

        sResourceAppPackageName = strNewResPkgName;

        if (null != sResourceAppContext)
            try {
                sResources = sResourceAppContext.getResources();
            } catch (NullPointerException e) {
                e.printStackTrace();
                sResources = context.getResources();
            }

        return true;
    }

    public static boolean reloadResourcesByPkgName(Context context, String strNewResPkgName) {
        if (null == context)
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT);

        if (TextUtils.isEmpty(strNewResPkgName))
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_PKG_NAME);

        try {
            sResourceAppContext = context.getApplicationContext().createPackageContext(strNewResPkgName, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        sResourceAppPackageName = strNewResPkgName;

        if (null != sResourceAppContext)
            try {
                sResources = sResourceAppContext.getResources();
            } catch (NullPointerException e) {
                e.printStackTrace();
                sResources = context.getResources();
            }


        if (null != sResources) {
            // Sets language
            String savedLanguage = "unknown";

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            if (null != sharedPref)
                savedLanguage = sharedPref.getString(ResourceManager.SHARED_PREF_KEY_USER_LANGUAGE, "unknown");

            Log.i(LOG_TAG, "reloadResourcesByPkgName() - Saved Language: " + savedLanguage);

            if (!TextUtils.isEmpty(savedLanguage)) {
                Configuration config = sResources.getConfiguration();
                if (null != config) {
                    if (savedLanguage.equals(ResourceManager.KOREAN))
                        config.locale = Locale.KOREA;
                    else if (savedLanguage.equals(ResourceManager.CHINESE))
                        config.locale = Locale.CHINESE;
                    else
                        config.locale = Locale.ENGLISH;

                    sResources.updateConfiguration(config, null);
                }
            }
        }

        return true;
    }

    public static void setResourcesByResourceIds(Context context, View view, int[] localResourceIDs) {
        if (null == context)
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT);

        if (null == view)
            throw new NullPointerException("view is null.");

        if (null == localResourceIDs)
            throw new NullPointerException("localResourceIDs is null.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResources)
            getResourceAppResources(context);

        Resources localResources = context.getResources();
        for (int localResourceId : localResourceIDs) {
            String resourceEntryName;
            String resourceType;

            try {
                resourceEntryName = localResources.getResourceEntryName(localResourceId);
                resourceType = localResources.getResourceTypeName(localResourceId);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                continue;
            }

            int resourceId = sResources.getIdentifier(resourceEntryName, resourceType, sResourceAppPackageName);
            if (0 != resourceId) {
                switch (resourceType) {
                    case "drawable":
                        if (view instanceof ImageView)
                            ((ImageView) view).setImageDrawable(sResources.getDrawable(resourceId));
                        else if(view instanceof SeekBar) {
                            if (resourceEntryName.contains("progress"))
                                ((SeekBar) view).setProgressDrawable(sResources.getDrawable(resourceId));
                            else if (resourceEntryName.contains("thumb"))
                                ((SeekBar) view).setThumb(sResources.getDrawable(resourceId));
                        } else
                            view.setBackground(sResources.getDrawable(resourceId));

                        break;

                    case "string":
                        try {
                            ((TextView) view).setText(sResources.getString(resourceId));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case "color":
                        if (view instanceof TextView) {
                            TextView tv = (TextView) view;
                            Drawable drawable = tv.getBackground();
                            if (null != drawable && drawable instanceof ColorDrawable) {
                                tv.setBackgroundColor(sResources.getColor(resourceId));
                            } else {
                                ColorStateList csl = sResources.getColorStateList(resourceId);
                                if (null != csl) // just in case
                                    tv.setTextColor(csl);
                            }
                        } else
                            view.setBackgroundColor(sResources.getColor(resourceId));

                        break;
                }
            }
        }
    }

    public static View getLayoutByResourceId(Context context, int localLayoutResourceId) {
        if (null == context)
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT);

        if (0 == localLayoutResourceId)
            throw new IllegalArgumentException("localLayoutResourceId is zero.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResources)
            getResourceAppResources(context);

        Resources localResources = context.getResources();
        String resourceEntryName;
        String resourceType;

        try {
            resourceEntryName = localResources.getResourceEntryName(localLayoutResourceId);
            resourceType = localResources.getResourceTypeName(localLayoutResourceId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }

        View layout = null;
        int layoutId = sResources.getIdentifier(resourceEntryName, resourceType, sResourceAppPackageName);
        if (0 != layoutId)
            layout = LayoutInflater.from(sResourceAppContext).inflate(sResources.getLayout(layoutId), null);

        return layout;
    }

    public static View getViewByResourceId(Context context, View layout, int localResourceId) {
        if (null == context)
            throw new NullPointerException(NULL_POINTER_EXCEPTION_DESCRIPTION_CONTEXT);

        if (null == layout)
            throw new NullPointerException("layout is null.");

        if (0 == localResourceId)
            throw new IllegalArgumentException("localLayoutResourceId is zero.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResources)
            getResourceAppResources(context);

        Resources localResources = context.getResources();
        String resourceEntryName;
        String resourceType;

        try {
            resourceEntryName = localResources.getResourceEntryName(localResourceId);
            resourceType = localResources.getResourceTypeName(localResourceId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }

        View view = null;
        int resourceId = sResources.getIdentifier(resourceEntryName, resourceType, sResourceAppPackageName);
        if (0 != resourceId)
            view = layout.findViewById(resourceId);

        return view;
    }
}
