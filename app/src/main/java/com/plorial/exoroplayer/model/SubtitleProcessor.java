package com.plorial.exoroplayer.model;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;
import com.sri.subtitlessupport.utils.Caption;
import com.sri.subtitlessupport.utils.TimedTextObject;

import java.util.Collection;

/**
 * Created by plorial on 4/13/16.
 */
public class SubtitleProcessor implements Runnable {

    private Handler subtitleDisplayHandler;
    private EMVideoView emVideoView;
    private TextView subtitleText;
    private TimedTextObject srt;

    public SubtitleProcessor(Handler subtitleDisplayHandler, EMVideoView emVideoView, TextView subtitleText) {
        this.subtitleDisplayHandler = subtitleDisplayHandler;
        this.emVideoView = emVideoView;
        this.subtitleText = subtitleText;
    }

    @Override
    public void run() {
        if (emVideoView != null && emVideoView.isPlaying()) {

            long currentPos = emVideoView.getCurrentPosition();
            Collection<Caption> subtitles = srt.captions.values();
            for (Caption caption : subtitles) {
                if (currentPos >= caption.start.mseconds
                        && currentPos <= caption.end.mseconds) {
                    onTimedText(caption);
                    break;
                } else if (currentPos > caption.end.mseconds) {
                    onTimedText(null);
                }
            }
        }
        subtitleDisplayHandler.postDelayed(this, 100);
    }

    public void addSrt(TimedTextObject srt){
        this.srt = srt;
    }

    private void onTimedText(Caption text) {
        if (text == null) {
            subtitleText.setVisibility(View.INVISIBLE);
            return;
        }
        subtitleText.setText(Html.fromHtml(text.content));
        subtitleText.setVisibility(View.VISIBLE);
        Log.d("TAG", text.content);

    }
}
