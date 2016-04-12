package com.plorial.plorialplayer;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;

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
       subsFetchTask = new SubtitleProcessingTask(context,subtitleDisplayHandler,processor);

       subsFetchTask.execute();
    }


}
