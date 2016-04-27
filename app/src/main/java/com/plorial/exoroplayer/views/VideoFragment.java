package com.plorial.exoroplayer.views;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;


import com.devbrackets.android.exomedia.EMVideoView;
import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.controllers.SubtitlesController;
import com.plorial.exoroplayer.controllers.VideoControl;
import com.plorial.exoroplayer.model.events.VideoStatusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by plorial on 4/15/16.
 */
public class VideoFragment extends Fragment implements MediaPlayer.OnPreparedListener{

    private static final String TAG = VideoFragment.class.getSimpleName();

    public static final String VIDEO_PATH = "VIDEO_PATH";
    public static final String SRT1_PATH = "SRT1";
    public static final String SRT2_PATH = "SRT2";
    private EMVideoView emVideoView;
    private TextView firstSubtitleText;
    private TextView secondSubtitleText;
    private FrameLayout controlsHolder;

    private boolean doubleSubsReady = false;
    private String videoSource;
    private String srt1Source;
    private String srt2Source;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);

        firstSubtitleText = (TextView) view.findViewById(R.id.firstSubtitleText);
        secondSubtitleText = (TextView) view.findViewById(R.id.secondSubtitleText);
        emVideoView = (EMVideoView) view.findViewById(R.id.video_play_activity_video_view);
        controlsHolder = (FrameLayout) view.findViewById(R.id.controlsHolder);

        videoSource = getArguments().getString(VIDEO_PATH);
        srt1Source = getArguments().getString(SRT1_PATH);
        srt2Source = getArguments().getString(SRT2_PATH);
        Log.d(TAG, "video source " + videoSource);
        setupVideoView();
        return view;
    }

    private void setupVideoView() {
        emVideoView.setOnPreparedListener(this);
        emVideoView.setVideoPath(videoSource);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        prepareSubs();
        VideoControl videoControl = new VideoControl(emVideoView);
        final VideoControllerView controller = new VideoControllerView(getActivity());
        controller.setMediaPlayer(videoControl);
        controller.setAnchorView(controlsHolder);
       emVideoView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               controller.show(10000);
           }
       });
    }

    private void prepareSubs(){
        if (srt1Source == null && srt2Source == null){
            doubleSubsReady = true;
            EventBus.getDefault().post(new VideoStatusEvent(VideoStatusEvent.READY_TO_START));
            firstSubtitleText.setVisibility(View.GONE);
            secondSubtitleText.setVisibility(View.GONE);
        }else if (srt1Source == null || srt2Source == null){
            EventBus.getDefault().post(new VideoStatusEvent(VideoStatusEvent.READY_TO_START));
            if(srt1Source != null){
                SubtitlesController firstSubController = new SubtitlesController(getActivity(), emVideoView, firstSubtitleText, srt1Source);
                firstSubController.startSubtitles();
            }else if(srt2Source != null){
                SubtitlesController firstSubController = new SubtitlesController(getActivity(), emVideoView, firstSubtitleText, srt2Source);
                firstSubController.startSubtitles();
            }
            secondSubtitleText.setVisibility(View.GONE);
        }else {
            SubtitlesController firstSubController = new SubtitlesController(getActivity(), emVideoView, firstSubtitleText, srt1Source);
            firstSubController.startSubtitles();

            SubtitlesController secondSubController = new SubtitlesController(getActivity(), emVideoView, secondSubtitleText, srt2Source);
            secondSubController.startSubtitles();
        }
    }

    @Subscribe
    public void onVideoStatusEvent(VideoStatusEvent event){
        if(event.getMassage() == VideoStatusEvent.PAUSE && emVideoView.isPlaying()) {
            emVideoView.pause();
        }
        if(event.getMassage() == VideoStatusEvent.READY_TO_START && doubleSubsReady) {
            emVideoView.start();
        }
        if (event.getMassage() == VideoStatusEvent.READY_TO_START && !doubleSubsReady) {
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
