package com.plorial.plorialplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    private EMVideoView emVideoView;
    private TextView subtitleText;

    String srtSource = "https://drive.google.com/open?id=0B9WKllES7zN7akFEeVRSNGdsb1E";
    String videoSource = "http://88.150.128.154/temp/2Whnvt4Nulrk2lnAs_LB6Q/1460512111/igra_prestolov/org/s4/1.mp4";
//    String videoSource = "https://r3---sn-03guxaxjvh-3c2e.googlevideo.com/videoplayback?pl=18&sver=3&expire=1460490289&upn=91pJlPSQUes&mime=video%2Fmp4&initcwndbps=596250&ratebypass=yes&sparams=dur%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpl%2Cratebypass%2Crequiressl%2Csource%2Cupn%2Cexpire&ipbits=0&requiressl=yes&mn=sn-03guxaxjvh-3c2e&mm=31&mt=1460468485&dur=215.132&id=o-AIHjdRP0Jin3yIylfaZbjYqtWG0VjiYFii5StMYM8z5P&lmt=1427892565302897&key=yt6&ip=91.124.46.184&mv=m&source=youtube&ms=au&itag=18&signature=1486F89F751F37DA29A45AC9E8024DDED79E38F0.35A30E9984BBADC21558514356114C9947014773&title=Nirvana%20-%20Come%20As%20You%20Are%20(Live%20at%20Reading%201992)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subtitleText = (TextView) findViewById(R.id.subtitleText);

        setupVideoView();
    }

    private void setupVideoView() {
        emVideoView = (EMVideoView)findViewById(R.id.video_play_activity_video_view);
        emVideoView.setOnPreparedListener(this);

        emVideoView.setVideoURI(Uri.parse(videoSource));
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        SubtitlesController subController = new SubtitlesController(this,emVideoView,subtitleText);
        subController.startSubtitles();

        emVideoView.start();
    }
}
