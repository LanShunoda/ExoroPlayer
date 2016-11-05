package com.plorial.exoroplayer.controllers;

import com.plorial.exoroplayer.ijk.IjkVideoView;
import com.plorial.exoroplayer.views.VideoControllerView;

/**
 * Created by plorial on 4/26/16.
 */
public class VideoControl implements VideoControllerView.MediaPlayerControl {

    private IjkVideoView videoView;

    public VideoControl(IjkVideoView videoView) {
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
    public long getDuration() {
        return videoView.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return videoView.getCurrentPosition();
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
}
