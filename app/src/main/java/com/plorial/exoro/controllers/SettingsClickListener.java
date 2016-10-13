package com.plorial.exoro.controllers;

import android.app.Activity;
import android.view.View;

import com.plorial.exoro.R;
import com.plorial.exoro.model.events.VideoStatusEvent;
import com.plorial.exoro.views.SettingsDialog;

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
