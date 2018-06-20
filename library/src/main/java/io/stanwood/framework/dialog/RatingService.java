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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RatingService {

    private Context appContext;
    private long launchTimes;

    public RatingService(Context context) {
        this.appContext = context.getApplicationContext();
        init();
    }

    public boolean shouldBeDisplayed() {
        if (!isConnectedNow()) return false;
        return launchTimes > 0 && !Preferences.getDialogShown(appContext) && Preferences.getLaunchTimes(appContext) >= launchTimes;
    }

    public RatingService setLaunchTimes(long launchTimes) {
        this.launchTimes = launchTimes;
        return this;
    }

    private void init() {
        long counter = Preferences.getLaunchTimes(appContext);
        if (counter == 0) {
            Preferences.storeStartDate(appContext);
        }
        Preferences.storeLaunchTimes(appContext, counter + 1);
    }

    public void reset() {
        Preferences.reset(appContext);
    }

    public boolean isConnectedNow() {
        ConnectivityManager conMgr = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr == null) return false;
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
