package com.swlee.android.runtimetheme;

import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
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

    private static String sResourceAppPackageName;
    private static Context sResourceAppContext;
    private static Resources sResourceAppResources;

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
                    sResourceAppResources = sResourceAppContext.getResources();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sResourceAppResources = context.getResources();
                }
            }

            if (null != sResourceAppResources) {
                // Sets language
                String savedLanguage = "unknown";

                if (null != sharedPref)
                    savedLanguage = sharedPref.getString(ResourceManager.SHARED_PREF_KEY_USER_LANGUAGE, "unknown");

                Log.i(LOG_TAG, "onCreate() - Saved Language: " + savedLanguage);

                if (!TextUtils.isEmpty(savedLanguage)) {
                    Configuration config = sResourceAppResources.getConfiguration();
                    if (null != config) {
                        if (savedLanguage.equals(ResourceManager.KOREAN))
                            config.locale = Locale.KOREA;
                        else if (savedLanguage.equals(ResourceManager.CHINESE))
                            config.locale = Locale.CHINESE;
                        else
                            config.locale = Locale.ENGLISH;

                        sResourceAppResources.updateConfiguration(config, null);
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
            throw new NullPointerException("context is null.");

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
            throw new NullPointerException("context is null.");

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
            throw new NullPointerException("context is null.");

        if (null == sResourceAppResources) {
            Context resourceAppContext = getResourceAppContext(context);
            if (null != resourceAppContext)
                sResourceAppResources = resourceAppContext.getResources();
            else
                sResourceAppResources = context.getResources();
        }

        return sResourceAppResources;
    }

    public static boolean reloadResources(Context context, String strNewResPkgName) {
        if (null == context)
            throw new NullPointerException("context is null.");

        if (TextUtils.isEmpty(strNewResPkgName))
            throw new NullPointerException("strNewResPkgName is null.");

        try {
            sResourceAppContext = context.getApplicationContext().createPackageContext(strNewResPkgName, 0);
        } catch (Exception e) {
            e.printStackTrace();
            sResourceAppPackageName = context.getPackageName();
            sResourceAppContext = context;
            sResourceAppResources = context.getResources();
            return false;
        }

        sResourceAppPackageName = strNewResPkgName;

        if (null != sResourceAppContext)
            try {
                sResourceAppResources = sResourceAppContext.getResources();
            } catch (NullPointerException e) {
                e.printStackTrace();
                sResourceAppPackageName = context.getPackageName();
                sResourceAppContext = context;
                sResourceAppResources = context.getResources();
            }

        return true;
    }

    public static boolean reloadResourcesByPkgName(Context context, String strNewResPkgName) {
        if (null == context)
            throw new NullPointerException("context is null.");

        if (TextUtils.isEmpty(strNewResPkgName))
            throw new NullPointerException("strNewResPkgName is null.");

        try {
            sResourceAppContext = context.getApplicationContext().createPackageContext(strNewResPkgName, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        sResourceAppPackageName = strNewResPkgName;

        if (null != sResourceAppContext)
            try {
                sResourceAppResources = sResourceAppContext.getResources();
            } catch (NullPointerException e) {
                e.printStackTrace();
                sResourceAppResources = context.getResources();
            }


        if (null != sResourceAppResources) {
            // Sets language
            String savedLanguage = "unknown";

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            if (null != sharedPref)
                savedLanguage = sharedPref.getString(ResourceManager.SHARED_PREF_KEY_USER_LANGUAGE, "unknown");

            Log.i(LOG_TAG, "reloadResourcesByPkgName() - Saved Language: " + savedLanguage);

            if (!TextUtils.isEmpty(savedLanguage)) {
                Configuration config = sResourceAppResources.getConfiguration();
                if (null != config) {
                    if (savedLanguage.equals(ResourceManager.KOREAN))
                        config.locale = Locale.KOREA;
                    else if (savedLanguage.equals(ResourceManager.CHINESE))
                        config.locale = Locale.CHINESE;
                    else
                        config.locale = Locale.ENGLISH;

                    sResourceAppResources.updateConfiguration(config, null);
                }
            }
        }

        return true;
    }

    public static View findLayoutById(Context context, int localLayoutResourceId) {
        if (null == context)
            throw new NullPointerException("context is null.");

        if (0 == localLayoutResourceId)
            throw new IllegalArgumentException("localLayoutResourceId is zero.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResourceAppResources)
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
        int layoutId = sResourceAppResources.getIdentifier(resourceEntryName, resourceType, sResourceAppPackageName);
        if (0 != layoutId)
            layout = LayoutInflater.from(sResourceAppContext).inflate(sResourceAppResources.getLayout(layoutId), null);

        return layout;
    }

    public static View findViewById(Context context, View layout, int localResourceId) {
        if (null == context)
            throw new NullPointerException("context is null.");

        if (null == layout)
            throw new NullPointerException("layout is null.");

        if (0 == localResourceId)
            throw new IllegalArgumentException("localLayoutResourceId is zero.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResourceAppResources)
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
        int resourceId = sResourceAppResources.getIdentifier(resourceEntryName, resourceType, sResourceAppPackageName);
        if (0 != resourceId)
            view = layout.findViewById(resourceId);

        return view;
    }

    /**
     * Finds a view that was identified by the id attribute from the XML that
     * was processed in onCreate in Activity. And then get resources by localResourceIDs
     * from the RuntimeThemeResource app.
     *
     * This should be called from onCreate in Activity when you do not use layout in RuntimeThemeResource app.
     *
     * @param context               context of Activity
     * @param localViewId           the id attribute from the layout XML in this app (VHS App)
     * @param localResourceIDs      resource id array: resource id of image, color and so on for the found view.
     * @return                      The view if found or null otherwise.
     */
    public static View findViewById(Context context, int localViewId, int[] localResourceIDs) {
        if (null == context)
            throw new NullPointerException("context is null.");

        if (null == localResourceIDs)
            throw new NullPointerException("localResourceIDs is null.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResourceAppResources)
            getResourceAppResources(context);

        View view = ((Activity)context).findViewById(localViewId);

        if (null != view && !sResourceAppPackageName.equalsIgnoreCase(context.getPackageName()))
            setResourcesByResourceIds(context, view, localResourceIDs);

        return view;
    }

    /**
     * Finds a view that was identified by the id attribute from the XML that
     * was processed in getView of Adapter or in onCreateView of Fragment.
     * And then get resources by localResourceIDs from the (VHS)ResourceApp.
     *
     * This should be called from getView of Adapter or onCreateView of Fragment
     * when you do not use layout in RuntimeThemeResource app.
     *
     * @param context               context of Activity
     * @param rootView              inflated view that was processed in getView or onCreateView
     * @param localViewId           the id attribute from the layout XML in this app (VHS App)
     * @param localResourceIDs      resource id array: resource id of image, color and so on for the found view.
     * @return                      The view if found or null otherwise.
     */
    public static View findViewById(Context context, View rootView, int localViewId, int[] localResourceIDs) {
        if (null == context)
            throw new NullPointerException("context is null.");

        if (null == rootView)
            throw new NullPointerException("rootView is null.");

        if (null == localResourceIDs)
            throw new NullPointerException("localResourceIDs is null.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResourceAppResources)
            getResourceAppResources(context);

        View view = rootView.findViewById(localViewId);

        if (null != view && !sResourceAppPackageName.equalsIgnoreCase(context.getPackageName()))
            setResourcesByResourceIds(context, view, localResourceIDs);

        return view;
    }

    public static void setResourcesByResourceIds(Context context, View view, int[] localResourceIDs) {
        if (null == context)
            throw new NullPointerException("context is null.");

        if (null == view)
            throw new NullPointerException("view is null.");

        if (null == localResourceIDs)
            throw new NullPointerException("localResourceIDs is null.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResourceAppResources)
            getResourceAppResources(context);

        Resources localResources = context.getResources();
        String viewClassName = view.getClass().getSimpleName();
        Log.d(LOG_TAG, "setResourcesByResourceIds - Class name: " + viewClassName);

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

            try {
                int resourceId = sResourceAppResources.getIdentifier(resourceEntryName, resourceType, sResourceAppPackageName);
                if (0 != resourceId) {
                    switch (resourceType) {
                        case "drawable": // Resources class already has Drawable Cache.
                            switch (viewClassName) {
                                case "ImageView":
                                case "ImageButton":
                                    if (resourceEntryName.endsWith("_bg"))
                                        view.setBackground(sResourceAppResources.getDrawable(resourceId));
                                    else
                                        ((ImageView) view).setImageDrawable(sResourceAppResources.getDrawable(resourceId));
                                    break;

                                case "ProgressBar":
                                case "SeekBar":
                                    if (resourceEntryName.endsWith("_progress"))
                                        ((ProgressBar) view).setProgressDrawable(sResourceAppResources.getDrawable(resourceId));
                                    else if (resourceEntryName.endsWith("_thumb"))
                                        ((SeekBar) view).setThumb(sResourceAppResources.getDrawable(resourceId));
                                    else
                                        view.setBackground(sResourceAppResources.getDrawable(resourceId));
                                    break;

                                case "Switch":
                                    if (resourceEntryName.endsWith("_track"))
                                        ((Switch) view).setTrackDrawable(sResourceAppResources.getDrawable(resourceId));
                                    else if (resourceEntryName.endsWith("_thumb"))
                                        ((Switch) view).setThumbDrawable(sResourceAppResources.getDrawable(resourceId));
                                    else
                                        view.setBackground(sResourceAppResources.getDrawable(resourceId));
                                    break;

                                default:
                                    view.setBackground(sResourceAppResources.getDrawable(resourceId));
                            }
                            break;

                        case "string":
                            String str = sResourceAppResources.getString(resourceId);
                            if (!TextUtils.isEmpty(str)) // just in case
                                ((TextView) view).setText(str);
                            break;

                        case "color":
                            switch (viewClassName) {
                                case "TextView":
                                case "Button":
                                case "ToggleButton":
                                    if (resourceEntryName.endsWith("_bg"))
                                        view.setBackgroundColor(sResourceAppResources.getColor(resourceId));
                                    else {
                                        // It is ok whether it is a simple color or a color state list.
                                        ColorStateList csl = sResourceAppResources.getColorStateList(resourceId);
                                        if (null != csl) // just in case
                                            ((TextView) view).setTextColor(csl);
                                    }
                                    break;

                                case "Switch":
                                    ((Switch) view).setTextColor(sResourceAppResources.getColorStateList(resourceId));
                                    break;

                                default:
                                    view.setBackgroundColor(sResourceAppResources.getColor(resourceId));
                            }
                            break;

                        default:
                            Log.e(LOG_TAG, "setResourcesByResourceIds - No match resource type.");
                    }
                }
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getString(Context context, int localResourceId) {
        String str = null;

        if (null == context)
            throw new NullPointerException("context is null.");

        if (TextUtils.isEmpty(sResourceAppPackageName))
            getResourceAppPackageName(context);

        if (null == sResourceAppResources)
            getResourceAppResources(context);

        Resources localResources = context.getResources();
        if (!sResourceAppPackageName.equalsIgnoreCase(context.getPackageName())) {
            try {
                String resourceEntryName = localResources.getResourceEntryName(localResourceId);
                String resourceType = localResources.getResourceTypeName(localResourceId);
                int resourceId = sResourceAppResources.getIdentifier(resourceEntryName, resourceType, sResourceAppPackageName);
                if (0 != resourceId) {
                    str = sResourceAppResources.getString(resourceId);
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        } else {
            str = localResources.getString(localResourceId);
        }

        return str;
    }
}
