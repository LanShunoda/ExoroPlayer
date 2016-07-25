package com.plorial.exoroplayer.controllers;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.widget.TextView;

import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.plorial.exoroplayer.model.SubtitleProcessingTask;
import com.plorial.exoroplayer.model.SubtitleProcessor;
import com.sri.subtitlessupport.utils.TimedTextObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by plorial on 4/13/16.
 */
public class SubtitlesController {

    private Context context;
    private TextView subtitlesText;
    private EMVideoView videoView;
    private String srtSource;
    private Handler subtitleDisplayHandler;
    private TextView tvTranslatedText;

    public SubtitlesController(Context context, EMVideoView videoView, TextView subtitlesText, String srtSource, TextView tvTranslatedText) {
        this.context = context;
        this.videoView = videoView;
        this.subtitlesText = subtitlesText;
        this.srtSource = srtSource;
        this.tvTranslatedText = tvTranslatedText;
    }

    public void startSubtitles(){
        subtitleDisplayHandler = new Handler();

        subtitlesText.setOnClickListener(new SubtitlesClickListener(tvTranslatedText));

        SubtitleProcessingTask subsFetchTask = new SubtitleProcessingTask(context);
        subsFetchTask.execute(srtSource);
        TimedTextObject srt = null;
        try {
            srt = subsFetchTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        SubtitleProcessor processor = new SubtitleProcessor(subtitleDisplayHandler, videoView, subtitlesText, srt);
        subtitleDisplayHandler.post(processor);
    }

    public void stopSubtitles(){
        subtitleDisplayHandler.removeCallbacksAndMessages(null);
    }
}
