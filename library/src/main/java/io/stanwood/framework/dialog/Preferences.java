/*
 * Copyright (c) 2018 stanwood Gmbh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.stanwood.framework.dialog;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public final class Preferences {

    private static final String KEY_START_DATE = "START_DATE";
    private static final String KEY_REMIND_DATE = "REMIND_DATE";
    private static final String KEY_LAUNCH_COUNTER = "LAUNCH_COUNTER";
    private static final String KEY_RATED = "RATED";
    private static final String KEY_DIALOG_SHOWN = "KEY_DIALOG_SHOWN";

    private Preferences() {
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("rating_dialog", Context.MODE_PRIVATE);
    }

    public static void storeStartDate(Context context) {
        getPreferences(context).edit().putLong(KEY_START_DATE, new Date().getTime()).apply();
    }

    public static long getStartDate(Context context) {
        return getPreferences(context).getLong(KEY_START_DATE, 0);
    }

    public static void storeRemindDate(Context context) {
        getPreferences(context).edit().putLong(KEY_REMIND_DATE, new Date().getTime()).apply();
    }

    public static long getRemindDate(Context context) {
        return getPreferences(context).getLong(KEY_REMIND_DATE, 0);
    }

    public static void storeLaunchTimes(Context context, long i) {
        getPreferences(context).edit().putLong(KEY_LAUNCH_COUNTER, i).apply();
    }

    public static long getLaunchTimes(Context context) {
        return getPreferences(context).getLong(KEY_LAUNCH_COUNTER, 0);
    }

    public static void storeRated(Context context, boolean b) {
        getPreferences(context).edit().putBoolean(KEY_RATED, b).apply();
    }

    public static boolean getRated(Context context) {
        return getPreferences(context).getBoolean(KEY_RATED, false);
    }

    public static void storeDialogShown(Context context, boolean b) {
        getPreferences(context).edit().putBoolean(KEY_DIALOG_SHOWN, b).apply();
    }

    public static boolean getDialogShown(Context context) {
        return getPreferences(context).getBoolean(KEY_DIALOG_SHOWN, false);
    }

    public static void reset(Context context) {
        getPreferences(context).edit().clear().apply();
    }

}
