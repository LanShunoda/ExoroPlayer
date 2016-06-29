package com.plorial.exoroplayer.controllers;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.Toast;

import com.plorial.exoroplayer.views.NavigationDrawerActivity;

/**
 * Created by plorial on 5/14/16.
 */
public class ErrorListener implements MediaPlayer.OnErrorListener {
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
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(context, "Error! Video file is bad! Or something else",Toast.LENGTH_LONG);
        handler.postDelayed(startFragment, 1000);
        return false;
    }
}
