package com.plorial.exoro.views;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.plorial.exoro.R;
import com.plorial.exoro.controllers.ErrorListener;
import com.plorial.exoro.controllers.SettingsClickListener;
import com.plorial.exoro.controllers.SubsChooseClickListener;
import com.plorial.exoro.controllers.SubtitlesController;
import com.plorial.exoro.controllers.VideoControl;
import com.plorial.exoro.model.ExUaUrlConvertorTask;
import com.plorial.exoro.model.SubsDownloader;
import com.plorial.exoro.model.events.VideoStatusEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by plorial on 6/25/16.
 */
public class VideoActivity extends AppCompatActivity implements SettingsDialog.OnUpdateSettingsListener, OnPreparedListener {

    private static final String TAG = VideoActivity.class.getSimpleName();

    public static final String VIDEO_PATH = "VIDEO_PATH";
    public static final String SRT1_PATH = "SRT1";
    public static final String SRT2_PATH = "SRT2";
    public static final String SUB_REF = "SUB_REF";

    public static int subsCorrector = 0;

    private EMVideoView emVideoView;
    private FrameLayout controlsHolder;
    private long currentPos;
    private HashMap<Integer, SubtitlesController> subtitlesControllers;
    private FirebaseAnalytics firebaseAnalytics;

    private String videoSource;
    private String srt1Source;
    private String srt2Source;
    private TextView tvTranslatedText;
    private ProgressBar pbPreparing;
    public static InterstitialAd ad;
    private int uiFlags;
    private LinearLayout subContainer;
    public File[] subs;
    public boolean[] checkedItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        setUpViews();
        if(savedInstanceState != null){
            currentPos = savedInstanceState.getLong("CURRENT_POS");
            checkedItems = savedInstanceState.getBooleanArray("CURRENT_SUBS");
        }
        setUpAd();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }else {
            uiFlags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiFlags);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int i) {
                decorView.setSystemUiVisibility(uiFlags);
            }
        });
    }

    private void setUpAd() {
        ad = new InterstitialAd(this);
        ad.setAdUnitId(getString(R.string.pause_banner_id));
    }

    private void setUpViews() {
        emVideoView = (EMVideoView) findViewById(R.id.video_play_activity_video_view);
        controlsHolder = (FrameLayout) findViewById(R.id.controlsHolder);
        subContainer = (LinearLayout) findViewById(R.id.subsContainer);
        tvTranslatedText = (TextView) findViewById(R.id.tvTranslatedText);
        pbPreparing = (ProgressBar) findViewById(R.id.pbPreparing);
        tvTranslatedText.setText(R.string.preparing_video);
        videoSource = getIntent().getExtras().getString(VIDEO_PATH);
        srt1Source = getIntent().getExtras().getString(SRT1_PATH);
        srt2Source = getIntent().getExtras().getString(SRT2_PATH);
        Log.d(TAG, "video source " + videoSource);

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle analytics = new Bundle();
        analytics.putString(FirebaseAnalytics.Param.ITEM_NAME,"Video start playing, src: " + videoSource);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, analytics);

        getPreferences();
        setupVideoView();
        downloadSubs();
    }

    private void setupVideoView() {
        emVideoView.setOnErrorListener(new ErrorListener(this));
        emVideoView.setOnPreparedListener(this);
        if(videoSource.contains("http://www.ex.ua/")) {
            ExUaUrlConvertorTask task = new ExUaUrlConvertorTask();
            task.execute(videoSource);
            try {
                videoSource = task.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, videoSource);
        emVideoView.setVideoPath(videoSource);
        emVideoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                if(ad.isLoaded()){
                    ad.show();
                }
            }
        });
    }

    @Override
    public void onPrepared() {
        prepareSubs();
        VideoControl videoControl = new VideoControl(emVideoView);
        final VideoControllerView controller = new VideoControllerView(this);
        controller.setMediaPlayer(videoControl);
        controller.setAnchorView(controlsHolder);
        emVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.show(10000);
            }
        });
        AppCompatImageButton settingsButton = (AppCompatImageButton) controller.getRoot().findViewById(R.id.settings_button);
        AppCompatImageButton subsButton = (AppCompatImageButton) controller.getRoot().findViewById(R.id.subs_button);
        settingsButton.setOnClickListener(new SettingsClickListener(this));
        subsButton.setOnClickListener(new SubsChooseClickListener(this));
        emVideoView.seekTo((int) currentPos);
        emVideoView.start();
        tvTranslatedText.setVisibility(View.INVISIBLE);
        pbPreparing.setVisibility(View.INVISIBLE);
    }

    private void downloadSubs() {
        String subRef = getIntent().getExtras().getString(SUB_REF);
        if(subRef != null) {
            SubsDownloader subsDownloader = new SubsDownloader(this);
            subsDownloader.execute(subRef);
            try {
                subs = subsDownloader.get();
            if(subs != null) {
                checkedItems = new boolean[subs.length];
            }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareSubs() {
        subtitlesControllers = new HashMap<>();
        if(srt1Source != null){
            addSub(srt1Source, 1);
        }
        if(srt2Source != null){
            addSub(srt2Source, 2);
        }
    }

    public void updateSubs(){
        for (int i = 0; i < checkedItems.length; i++){
            if (checkedItems[i]){
                if(!subtitlesControllers.containsKey(i)) {
                    addSub(subs[i].getAbsolutePath(), i);
                }
            } else {
                if(subtitlesControllers.containsKey(i)) {
                    removeSub(i);
                }
            }
        }
    }

    private void removeSub(int pos){
        SubtitlesController controller = subtitlesControllers.get(pos);
        controller.stopSubtitles();
        View removeView = subContainer.findViewWithTag(pos);
        subContainer.removeView(removeView);
        subtitlesControllers.remove(pos);
    }

    public void addSub(String srtSource, int pos){
        View view = this.getLayoutInflater().inflate(R.layout.subs_text_view_item, subContainer, false);
        TextView subTextView = (TextView) view.findViewById(R.id.subTextView);
        subTextView.setTag(pos);
        subContainer.addView(subTextView);
        SubtitlesController subController = new SubtitlesController(this, emVideoView, subTextView, srtSource, tvTranslatedText);
        subtitlesControllers.put(pos, subController);
        subController.startSubtitles();
    }

    private void stopAllSubs(){
        if(subtitlesControllers != null) {
            for (Map.Entry<Integer, SubtitlesController> entry : subtitlesControllers.entrySet()) {
                entry.getValue().stopSubtitles();
            }
        }
    }

    public void getPreferences() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        float textSize = preferences.getFloat("TEXT_SIZE", 20);
        int alpha = preferences.getInt("TEXT_ALPHA", 125);
        updateTextSize(textSize);
        updateTextAlpha(alpha);
    }

    private void updateTextSize(float textSize) {
        int subsCount = subContainer.getChildCount();
        for (int i = 0; i < subsCount; i++){
            ((TextView)subContainer.getChildAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
        tvTranslatedText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    private void updateTextAlpha(int alpha){
        int subsCount = subContainer.getChildCount();
        for (int i = 0; i < subsCount; i++){
            (subContainer.getChildAt(i)).setBackgroundColor(Color.argb(alpha, 00, 00, 00));
        }
        tvTranslatedText.setBackgroundColor(Color.argb(alpha, 00, 00, 00));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("CURRENT_POS",emVideoView.getCurrentPosition());
        outState.putBooleanArray("CURRENT_SUBS", checkedItems);
    }

    @Subscribe
    public void onVideoStatusEvent(VideoStatusEvent event){
        if(event.getMassage() == VideoStatusEvent.PAUSE && emVideoView.isPlaying()) {
            emVideoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAd();
    }

    public void loadAd(){
        AdRequest adRequest = new AdRequest.Builder().build();
        ad.loadAd(adRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        stopAllSubs();
        emVideoView.release();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onUpdateSettings() {
        getPreferences();
    }
}
