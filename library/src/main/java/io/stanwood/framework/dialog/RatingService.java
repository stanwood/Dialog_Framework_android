/*
 * Copyright 2018 stanwood Gmbh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
