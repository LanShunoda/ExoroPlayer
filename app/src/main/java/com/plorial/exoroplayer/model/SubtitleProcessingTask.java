package com.plorial.exoroplayer.model;

/**
 * Created by plorial on 4/12/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;
import com.plorial.exoroplayer.events.VideoStatusEvent;
import com.sri.subtitlessupport.utils.FormatSRT;
import com.sri.subtitlessupport.utils.TimedTextObject;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SubtitleProcessingTask extends AsyncTask<Void, Void, Void> {

    private Handler subtitleDisplayHandler;
    private Context context;
    private TimedTextObject srt;
    private SubtitleProcessor processor;
    private int srtSource;
    private static final String ACCESS_TOKEN = "JGh6jvKiMx0AAAAAAAAAB7KZNdxdlbj-zSrr23-gyzRK50HYrns2rpb46b6Zvdjq";

    public SubtitleProcessingTask(Context context,Handler subtitleDisplayHandler, SubtitleProcessor processor, int srtSource) {
        this.subtitleDisplayHandler = subtitleDisplayHandler;
        this.context = context;
        this.processor = processor;
        this.srtSource = srtSource;
    }

    @Override
    protected Void doInBackground(Void... params) {
        int count;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
				/*
				 * if you want to download file from Internet, use commented
				 * code.
				 */
//            URL url = new URL(srtSource);
//            is = url.openStream();
            is = getDropBoxFile();
            File f = getExternalFile();
            fos = new FileOutputStream(f);
            byte data[] = new byte[1024];
            while ((count = is.read(data)) != -1) {
                fos.write(data, 0, count);
            }
//            InputStream stream = context.getResources().openRawResource(
//                    R.raw.game);
            InputStream stream = new FileInputStream(f);
            FormatSRT formatSRT = new FormatSRT();
            srt = formatSRT.parseFile("sample.srt", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(is != null && fos != null) {
                    is.close();
                    fos.close();
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
            EventBus.getDefault().post(new VideoStatusEvent(VideoStatusEvent.READY_TO_START));
        }
        super.onPostExecute(result);
    }

    private InputStream getDropBoxFile(){
        try {
            DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
            DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
            FullAccount account = null;

            account = client.users().getCurrentAccount();

            ListFolderResult result = client.files().listFolder("");
            while (true) {
                for (Metadata metadata : result.getEntries()) {
                    System.out.println(metadata.getPathLower());
                }

                if (!result.getHasMore()) {
                    break;
                }

                result = client.files().listFolderContinue(result.getCursor());
            }
            DbxDownloader<FileMetadata> downloader = client.files().download(result.getEntries().get(srtSource).getPathLower());
            return downloader.getInputStream();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }


    private File getExternalFile() {
        File srt = null;
        try {
            srt = new File(context.getApplicationContext().getExternalFilesDir(null)
                    .getPath() + "sample" + srtSource +".srt");
            if(srt.exists()) srt.delete();
            srt.createNewFile();
            return srt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

