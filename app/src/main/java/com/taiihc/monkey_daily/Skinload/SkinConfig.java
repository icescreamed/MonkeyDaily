package com.taiihc.monkey_daily.Skinload;

import android.content.Context;

import com.taiihc.monkey_daily.Utils.PreferencesUtils;

/**
 * 动态换肤的配置
 */
public class SkinConfig {
    public static final String NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String SKIN_SUFFIX = ".theme";
    public static final String SKIN_FOLER_NAME = "skin";
    public static final String PREF_CUSTOM_SKIN_PATH = "md_skin_custom_path";
    public static final String DEFALT_SKIN = "md_skin_default";
    public static final String SKIN_FROM = "md_skin_from";
    public static final int FROM_INTERNAL = 0;
    public static final int FROM_EXTERNAL = 1;
    public static final String ATTR_SKIN_ENABLE = "enable";

    /**
     * get path of last skin package path
     *
     * @param context
     * @return path of skin package
     */
    public static String getCustomSkinPath(Context context) {
        return PreferencesUtils.getString(context, PREF_CUSTOM_SKIN_PATH, DEFALT_SKIN);
    }

    public static void saveSkinPath(Context context, String path) {
        PreferencesUtils.putString(context, PREF_CUSTOM_SKIN_PATH, path);
    }

    public static boolean isDefaultSkin(Context context) {
        return DEFALT_SKIN.equals(getCustomSkinPath(context));
    }
}
