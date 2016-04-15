package com.plorial.exoroplayer.views;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;
import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.controllers.SubtitlesController;
import com.plorial.exoroplayer.events.VideoStatusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by plorial on 4/15/16.
 */
public class VideoFragment extends Fragment implements MediaPlayer.OnPreparedListener{


    private EMVideoView emVideoView;
    private TextView firstSubtitleText;
    private TextView secondSubtitleText;

    String videoSource = "http://88.150.128.154/temp/PI0sv7uatPIGSkctf7QjgA/1460779524/igra_prestolov/org/s4/1.mp4";
    private boolean doubleSubsReady = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);

        firstSubtitleText = (TextView) view.findViewById(R.id.firstSubtitleText);
        secondSubtitleText = (TextView) view.findViewById(R.id.secondSubtitleText);
        emVideoView = (EMVideoView) view.findViewById(R.id.video_play_activity_video_view);
        setupVideoView();
        return view;
    }

    private void setupVideoView() {

        emVideoView.setOnPreparedListener(this);

        emVideoView.setVideoURI(Uri.parse(videoSource));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        SubtitlesController firstSubController = new SubtitlesController(getActivity(), emVideoView, firstSubtitleText, 0);
        firstSubController.startSubtitles();

        SubtitlesController secondSubController = new SubtitlesController(getActivity(), emVideoView, secondSubtitleText, 1);
        secondSubController.startSubtitles();
    }

    @Subscribe
    public void onVideoStatusEvent(VideoStatusEvent event){

        Log.d("TAG", "onVideoStatusEvent");
        if(event.getMassage() == VideoStatusEvent.PAUSE && emVideoView.isPlaying()) {
            emVideoView.pause();
        }
        if(event.getMassage() == VideoStatusEvent.READY_TO_START && doubleSubsReady) {
            Log.d("TAG", "onVideoStatusEvent" + doubleSubsReady);
            emVideoView.start();
        }
        if (event.getMassage() == VideoStatusEvent.READY_TO_START && !doubleSubsReady) {
            Log.d("TAG", "onVideoStatusEvent" + doubleSubsReady);
            doubleSubsReady = true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        emVideoView.release();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
