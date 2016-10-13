package com.plorial.exoro.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.devbrackets.android.exomedia.listener.OnErrorListener;
import com.google.firebase.crash.FirebaseCrash;
import com.plorial.exoro.views.NavigationDrawerActivity;

/**
 * Created by plorial on 5/14/16.
 */
public class ErrorListener implements OnErrorListener {
    private Activity context;
    private Handler handler;
    private Runnable startFragment;

    public ErrorListener(Activity activity) {
        context = activity;
        handler = new Handler();
        startFragment = new Runnable() {
            @Override
            public void run() {
                startActivity();
            }
        };
    }

    private void startActivity() {
        Intent intent = new Intent(context, NavigationDrawerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onError() {
        FirebaseCrash.log("Error opening video");
        Toast.makeText(context, "Error! Video file is bad! Or something else",Toast.LENGTH_LONG);
        handler.postDelayed(startFragment, 1000);
        return false;
    }
}
