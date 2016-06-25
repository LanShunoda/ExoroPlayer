package com.plorial.exoroplayer.controllers;

import android.app.Activity;
import android.view.View;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.model.events.VideoStatusEvent;
import com.plorial.exoroplayer.views.SettingsDialog;
import com.plorial.exoroplayer.views.VideoActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by plorial on 6/25/16.
 */
public class SettingsClickListener implements View.OnClickListener {
    private Activity activity;

    public SettingsClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.settings_button:
                EventBus.getDefault().post(new VideoStatusEvent(VideoStatusEvent.PAUSE));
                new SettingsDialog().show(activity.getFragmentManager(),"SETTINGS");
                break;
        }
    }
}
