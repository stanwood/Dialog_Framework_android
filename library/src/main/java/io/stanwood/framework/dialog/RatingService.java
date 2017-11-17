package io.stanwood.framework.dialog;


import android.content.Context;

public class RatingService {

    static RatingService instance;

    Context appContext;
    long launchTimes;

    public static synchronized RatingService with(Context context) {
        if (instance == null) {
            instance = new RatingService(context.getApplicationContext());
        }
        return instance;
    }

    private RatingService(Context context) {
        this.appContext = context;
    }

    public RatingService setLaunchTimes(long launchTimes) {
        this.launchTimes = launchTimes;
        return this;
    }

    public void init() {
        long counter = Preferences.getLaunchTimes(appContext);
        if (counter == 0) {
            Preferences.storeStartDate(appContext);
        }
        Preferences.storeLaunchTimes(appContext, counter + 1);
    }

    public static boolean shouldBeDisplayed() {
        if (!Preferences.getRated(instance.appContext) &&
                Preferences.getLaunchTimes(instance.appContext) > instance.launchTimes) {
            return true;
        }
        return false;
    }

    public void reset() {
        Preferences.reset(appContext);
    }

}
