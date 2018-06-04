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
