package com.plorial.exoroplayer.controllers;

import android.widget.MediaController;

import com.devbrackets.android.exomedia.EMVideoView;

/**
 * Created by plorial on 4/26/16.
 */
public class VideoControl implements MediaController.MediaPlayerControl {

    private EMVideoView videoView;

    public VideoControl(EMVideoView videoView) {
        this.videoView = videoView;
    }

    @Override
    public void start() {
        videoView.start();
    }

    @Override
    public void pause() {
        videoView.pause();
    }

    @Override
    public int getDuration() {
        return (int) videoView.getDuration();
    }


    @Override
    public int getCurrentPosition() {
        return (int) videoView.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        videoView.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return videoView.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return videoView.getBufferPercentage();
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        throw new UnsupportedOperationException();
    }
}
