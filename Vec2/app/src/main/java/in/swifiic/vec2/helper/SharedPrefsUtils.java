package in.swifiic.vec2.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefsUtils {

    private static SharedPreferences getSharedPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setBooleanPreference(Context context, String key, boolean value) {
        getSharedPref(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
        return getSharedPref(context).getBoolean(key, defaultValue);
    }

    public static void setStringPreference(Context context, String key, String value) {
        getSharedPref(context).edit().putString(key, value).apply();
    }

    public static String getStringPreference(Context context, String key, String defaultValue) {
        return getSharedPref(context).getString(key, defaultValue);
    }

    public static void setIntegerPreference(Context context, String key, Integer value) {
        getSharedPref(context).edit().putInt(key, value).apply();
    }

    public static Integer getIntegerPreference(Context context, String key, Integer defaultValue) {
        return getSharedPref(context).getInt(key, defaultValue);
    }
}
