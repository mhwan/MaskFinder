package com.mhwan.mask.Util;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class AppContext extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();


    }
}
