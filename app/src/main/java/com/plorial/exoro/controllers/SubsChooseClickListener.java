package com.plorial.exoro.controllers;

import android.view.View;

import com.plorial.exoro.R;
import com.plorial.exoro.model.events.VideoStatusEvent;
import com.plorial.exoro.views.SubsChooseDialog;
import com.plorial.exoro.views.VideoActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by plorial on 6/29/16.
 */
public class SubsChooseClickListener implements View.OnClickListener {


    private VideoActivity videoActivity;

    public SubsChooseClickListener(VideoActivity videoActivity) {
        this.videoActivity = videoActivity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subs_button:
                EventBus.getDefault().post(new VideoStatusEvent(VideoStatusEvent.PAUSE));
                new SubsChooseDialog().show(videoActivity.getFragmentManager(),"SUBTITLES");
                break;
        }
    }
}
