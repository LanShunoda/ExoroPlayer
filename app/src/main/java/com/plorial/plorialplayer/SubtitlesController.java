package com.plorial.plorialplayer;

import android.content.Context;
import android.os.Handler;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devbrackets.android.exomedia.EMVideoView;

import java.text.BreakIterator;
import java.util.Locale;

/**
 * Created by plorial on 4/13/16.
 */
public class SubtitlesController {

    private SubtitleProcessingTask subsFetchTask;
    private Context context;
    private Handler subtitleDisplayHandler;
    private TextView subtitlesText;
    private EMVideoView videoView;

    public SubtitlesController(Context context, EMVideoView videoView, TextView subtitlesText) {
        this.context = context;
        this.videoView = videoView;
        this.subtitlesText = subtitlesText;
    }

    public void startSubtitles(){
       subtitleDisplayHandler = new Handler();
       SubtitleProcessor processor = new SubtitleProcessor(subtitleDisplayHandler,videoView, subtitlesText);

        SubtitlesClickListener subtitlesClickListener = new SubtitlesClickListener(videoView);
        subtitlesText.setOnClickListener(subtitlesClickListener);
       subsFetchTask = new SubtitleProcessingTask(context,subtitleDisplayHandler,processor);
       subsFetchTask.execute();
    }


}
