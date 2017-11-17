package io.stanwood.framework.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

public final class Preferences {

    private static final String KEY_START_DATE = "START_DATE";
    private static final String KEY_REMIND_DATE = "REMIND_DATE";
    private static final String KEY_LAUNCH_COUNTER = "LAUNCH_COUNTER";
    private static final String KEY_RATED = "RATED";

    private Preferences() {}

    public static void storeStartDate(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong(KEY_START_DATE, new Date().getTime()).apply();
    }

    public static long getStartDate(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(KEY_START_DATE, 0);
    }

    public static void storeRemindDate(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong(KEY_REMIND_DATE, new Date().getTime()).apply();
    }

    public static long getRemindDate(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(KEY_REMIND_DATE, 0);
    }

    public static void storeLaunchTimes(Context context, long i) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putLong(KEY_LAUNCH_COUNTER, i).apply();
    }

    public static long getLaunchTimes(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(KEY_LAUNCH_COUNTER, 0);
    }

    public static void storeRated(Context context, boolean b) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(KEY_RATED, b).apply();
    }

    public static boolean getRated(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(KEY_RATED, false);
    }

    public static void reset(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().clear().apply();
    }

}
