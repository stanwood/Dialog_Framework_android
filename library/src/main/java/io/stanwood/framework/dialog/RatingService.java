package io.stanwood.framework.dialog;


import android.content.Context;

public class RatingService {

    private Context appContext;
    private long launchTimes;

    public RatingService(Context context) {
        this.appContext = context.getApplicationContext();
        init();
    }

    public boolean shouldBeDisplayed() {
        return !Preferences.getDialogShown(appContext) && Preferences.getLaunchTimes(appContext) >= launchTimes;
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

}
