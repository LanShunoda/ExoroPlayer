package com.plorial.exoroplayer.views;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;
import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.controllers.SubtitlesController;
import com.plorial.exoroplayer.events.VideoStatusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private EMVideoView emVideoView;
    private TextView firstSubtitleText;
    private TextView secondSubtitleText;

    String videoSource = "http://88.150.128.154/temp/Fl-9vIzoTczBYb1VwaGpcQ/1460677644/igra_prestolov/org/s4/1.mp4";
    private boolean doubleSubsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstSubtitleText = (TextView) findViewById(R.id.firstSubtitleText);
        secondSubtitleText = (TextView) findViewById(R.id.secondSubtitleText);

        setupVideoView();
    }

    private void setupVideoView() {
        emVideoView = (EMVideoView)findViewById(R.id.video_play_activity_video_view);
        emVideoView.setOnPreparedListener(this);

        emVideoView.setVideoURI(Uri.parse(videoSource));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        SubtitlesController firstSubController = new SubtitlesController(this, emVideoView, firstSubtitleText, 0);
        firstSubController.startSubtitles();

        SubtitlesController secondSubController = new SubtitlesController(this, emVideoView, secondSubtitleText, 1);
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
