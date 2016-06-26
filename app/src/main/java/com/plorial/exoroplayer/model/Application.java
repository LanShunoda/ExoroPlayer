package com.plorial.exoroplayer.model;

import android.os.Build;

import com.karumi.dexter.Dexter;

/**
 * Created by plorial on 6/26/16.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.initialize(getApplicationContext());
        }
    }
}
