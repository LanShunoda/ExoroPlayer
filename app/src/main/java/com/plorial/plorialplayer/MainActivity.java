package com.plorial.plorialplayer;

import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.devbrackets.android.exomedia.EMVideoView;
import com.sri.subtitlessupport.utils.Caption;
import com.sri.subtitlessupport.utils.FormatSRT;
import com.sri.subtitlessupport.utils.TimedTextObject;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    public static final int FADE_OUT = 0;
    public static final int SHOW_PROGRESS = 1;

    private TextView subtitleText;
    private SubtitleProcessingTask subsFetchTask;
//    private MessageHandler mHandler;


    String srtSource = "https://drive.google.com/open?id=0B9WKllES7zN7akFEeVRSNGdsb1E";
    String videoSource = "http://88.150.128.154/temp/2Whnvt4Nulrk2lnAs_LB6Q/1460512111/igra_prestolov/org/s4/1.mp4";
//    String videoSource = "https://r3---sn-03guxaxjvh-3c2e.googlevideo.com/videoplayback?pl=18&sver=3&expire=1460490289&upn=91pJlPSQUes&mime=video%2Fmp4&initcwndbps=596250&ratebypass=yes&sparams=dur%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&ipbits=0&requiressl=yes&mn=sn-03guxaxjvh-3c2e&mm=31&mt=1460468485&dur=215.132&id=o-AIHjdRP0Jin3yIylfaZbjYqtWG0VjiYFii5StMYM8z5P&lmt=1427892565302897&key=yt6&ip=91.124.46.184&mv=m&source=youtube&ms=au&itag=18&signature=1486F89F751F37DA29A45AC9E8024DDED79E38F0.35A30E9984BBADC21558514356114C9947014773&title=Nirvana%20-%20Come%20As%20You%20Are%20(Live%20at%20Reading%201992)";
    EMVideoView emVideoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subtitleText = (TextView) findViewById(R.id.subtitleText);
//        mHandler = new MessageHandler();


        setupVideoView();

//        FrameLayout.LayoutParams rl2 = (FrameLayout.LayoutParams) subtitleText
//                .getLayoutParams();
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        rl2.bottomMargin = (int) (dm.heightPixels * 0.08);
//        subtitleText.setLayoutParams(rl2);
    }

    private void setupVideoView() {
        emVideoView = (EMVideoView)findViewById(R.id.video_play_activity_video_view);
        emVideoView.setOnPreparedListener(this);

        //For now we just picked an arbitrary item to play.  More can be found at
        //https://archive.org/details/more_animation
        emVideoView.setVideoURI(Uri.parse(videoSource));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        subsFetchTask = new SubtitleProcessingTask();
        subsFetchTask.execute();
        //Starts the video playback as soon as it is ready
        emVideoView.start();

//        mHandler.sendEmptyMessage(SHOW_PROGRESS);

    }

    public TimedTextObject srt;
    private Runnable subtitleProcessesor = new Runnable() {

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
    };
    private Handler subtitleDisplayHandler = new Handler();
    private boolean mDragging;

    public class SubtitleProcessingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            subtitleText.setText("Loading subtitles..");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
             int count;

            InputStream is = null;
            FileOutputStream fos = null;
            InputStream stream = null;
            try {
				/*
				 * if you want to download file from Internet, use commented
				 * code.
//				 */
//                 URL url = new URL(srtSource);
//                 is = url.openStream();
//                 File f = getExternalFile();
//                 fos = new FileOutputStream(f);
//                 byte data[] = new byte[1024];
//                 while ((count = is.read(data)) != -1) {
//                     fos.write(data, 0, count);
//                 }

                stream = getResources().openRawResource(
                        R.raw.game);
                FormatSRT formatSRT = new FormatSRT();
                srt = formatSRT.parseFile("game.srt", stream);

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(is != null && fos != null) {
                        is.close();
                        fos.close();
                        stream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (null != srt) {
                subtitleText.setText("");
                Toast.makeText(getApplicationContext(), "subtitles loaded!!",
                        Toast.LENGTH_SHORT).show();
                subtitleDisplayHandler.post(subtitleProcessesor);
                Log.d("TAG", "subtitles loaded!!");
            }
            super.onPostExecute(result);
        }
    }

    public void onTimedText(Caption text) {
        if (text == null) {
            subtitleText.setVisibility(View.INVISIBLE);
            return;
        }
        subtitleText.setText(Html.fromHtml(text.content));
        subtitleText.setVisibility(View.VISIBLE);
        Log.d("TAG", text.content);

    }

    public File getExternalFile() {
        File srt = null;
        try {
            srt = new File(getApplicationContext().getExternalFilesDir(null)
                    .getPath() + "/sample.srt");
            srt.createNewFile();
            return srt;
        } catch (Exception e) {

        }
        return null;
    }

//    private class MessageHandler extends Handler {
//
//        @Override
//        public void handleMessage(Message msg) {
//            if (emVideoView == null) {
//                return;
//            }
//
//            long pos;
//            switch (msg.what) {
//                case FADE_OUT:
//                    break;
//                case SHOW_PROGRESS:
//                    try {
//                        pos = setProgress();
//                    } catch (IllegalStateException ise) {
//                        ise.printStackTrace();
//                        break;
//                    }
//                    if (!mDragging && emVideoView.isPlaying()) {
//                        msg = obtainMessage(SHOW_PROGRESS);
//                        sendMessageDelayed(msg, 1000 - (pos % 1000));
//                    }
//                    break;
//            }
//        }
//    }
//
//    private long setProgress() {
//        if (emVideoView == null || mDragging) {
//            return 0;
//        }
//
//        long position = emVideoView.getCurrentPosition();
//        if (position == -1)
//            return 0;
//        long duration = emVideoView.getDuration();
//        if (/*mSeeker != null &&*/ duration > 0) {
//            long pos = 1000L * position / duration;
////            mSeeker.setProgress((int) pos);
//        }
//
//        return position;
//    }

}
