package com.sharpflux.deliveryboy2;

import android.app.Application;

public class MyApplication  extends Application {

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = true;
    }

    private static boolean activityVisible;

}
