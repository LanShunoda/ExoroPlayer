package com.plorial.plorialplayer;

/**
 * Created by plorial on 4/12/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.sri.subtitlessupport.utils.FormatSRT;
import com.sri.subtitlessupport.utils.TimedTextObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SubtitleProcessingTask extends AsyncTask<Void, Void, Void> {

    private Handler subtitleDisplayHandler;
    private Context context;
    private TimedTextObject srt;
    private SubtitleProcessor processor;

    public SubtitleProcessingTask(Context context,Handler subtitleDisplayHandler, SubtitleProcessor processor) {
        this.subtitleDisplayHandler = subtitleDisplayHandler;
        this.context = context;
        this.processor = processor;
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

            stream = context.getResources().openRawResource(
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
            Toast.makeText(context.getApplicationContext(), "subtitles loaded!!",
                    Toast.LENGTH_SHORT).show();
            processor.addSrt(srt);
            subtitleDisplayHandler.post(processor);
            Log.d("TAG", "subtitles loaded!!");

        }
        super.onPostExecute(result);
    }


    private File getExternalFile() {
        File srt = null;
        try {
            srt = new File(context.getApplicationContext().getExternalFilesDir(null)
                    .getPath() + "/sample.srt");
            srt.createNewFile();
            return srt;
        } catch (Exception e) {

        }
        return null;
    }
}

