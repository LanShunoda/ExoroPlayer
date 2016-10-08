package com.plorial.exoroplayer.model;

import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.plorial.exoroplayer.views.VideoActivity;
import com.sri.subtitlessupport.utils.Caption;
import com.sri.subtitlessupport.utils.TimedTextObject;

import java.util.Collection;

/**
 * Created by plorial on 4/13/16.
 */
public class SubtitleProcessor implements Runnable {

    private static final String TAG = SubtitleProcessor.class.getSimpleName();

    private Handler subtitleDisplayHandler;
    private EMVideoView emVideoView;
    private TextView subtitleText;
    private TimedTextObject srt;

    public SubtitleProcessor(Handler subtitleDisplayHandler, EMVideoView emVideoView, TextView subtitleText, TimedTextObject srt) {
        this.subtitleDisplayHandler = subtitleDisplayHandler;
        this.emVideoView = emVideoView;
        this.subtitleText = subtitleText;
        this.srt = srt;
    }

    @Override
    public void run() {
        if (emVideoView != null && emVideoView.isPlaying()) {
//            Log.d(TAG, "video pos " + emVideoView.getCurrentPosition() + " corrector " + VideoActivity.subsCorrector);
            long currentPos = emVideoView.getCurrentPosition() + VideoActivity.subsCorrector;
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

    private void onTimedText(Caption text) {
        if (text == null) {
            subtitleText.setVisibility(View.INVISIBLE);
            return;
        }
        subtitleText.setText(Html.fromHtml(text.content));
        subtitleText.setVisibility(View.VISIBLE);
    }
}
