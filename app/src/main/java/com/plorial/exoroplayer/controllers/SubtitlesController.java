package com.plorial.exoroplayer.controllers;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;
import com.plorial.exoroplayer.model.SubtitleProcessingTask;
import com.plorial.exoroplayer.model.SubtitleProcessor;

/**
 * Created by plorial on 4/13/16.
 */
public class SubtitlesController {

    private SubtitleProcessingTask subsFetchTask;
    private Context context;
    private Handler subtitleDisplayHandler;
    private TextView subtitlesText;
    private EMVideoView videoView;
    private int srtSource;

    public SubtitlesController(Context context, EMVideoView videoView, TextView subtitlesText, int srtSource) {
        this.context = context;
        this.videoView = videoView;
        this.subtitlesText = subtitlesText;
        this.srtSource = srtSource;
    }

    public void startSubtitles(){
        subtitleDisplayHandler = new Handler();
        SubtitleProcessor processor = new SubtitleProcessor(subtitleDisplayHandler, videoView, subtitlesText);

        SubtitlesClickListener subtitlesClickListener = new SubtitlesClickListener();
        subtitlesText.setOnClickListener(subtitlesClickListener);
        subsFetchTask = new SubtitleProcessingTask(context, subtitleDisplayHandler, processor, srtSource);
        subsFetchTask.execute();
    }
}
