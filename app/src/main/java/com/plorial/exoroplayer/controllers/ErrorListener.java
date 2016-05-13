package com.plorial.exoroplayer.controllers;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.Toast;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.views.FileExplorerFragment;

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
                startFragment();
            }
        };
    }

    private void startFragment() {
        FileExplorerFragment fragment = new FileExplorerFragment();
        FragmentTransaction transaction = context.getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Toast.makeText(context, "Error! Video file is bad! Or something else",Toast.LENGTH_LONG);
        handler.postDelayed(startFragment, 1000);
        return false;
    }
}
