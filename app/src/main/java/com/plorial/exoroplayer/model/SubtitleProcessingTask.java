package com.plorial.exoroplayer.model;

/**
 * Created by plorial on 4/12/16.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.plorial.exoroplayer.model.events.VideoStatusEvent;
import com.sri.subtitlessupport.utils.FatalParsingException;
import com.sri.subtitlessupport.utils.FormatASS;
import com.sri.subtitlessupport.utils.FormatSCC;
import com.sri.subtitlessupport.utils.FormatSRT;
import com.sri.subtitlessupport.utils.FormatSTL;
import com.sri.subtitlessupport.utils.FormatTTML;
import com.sri.subtitlessupport.utils.TimedTextObject;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SubtitleProcessingTask extends AsyncTask<String, Void, TimedTextObject> {

    private Context context;
    private TimedTextObject srt;

//    private static final String DROP_BOX_ACCESS_TOKEN = "JGh6jvKiMx0AAAAAAAAAB7KZNdxdlbj-zSrr23-gyzRK50HYrns2rpb46b6Zvdjq";

    public SubtitleProcessingTask(Context context) {
        this.context = context;

    }

    @Override
    protected TimedTextObject doInBackground(String... params) {
        String srtPath = params[0];
        File f = new File(srtPath);
        InputStream stream = null;
        try {
            stream = new FileInputStream(f);
            switch (SubtitlesUtil.getSubtitlesType(f.getAbsolutePath())){
                case SRT:
                    FormatSRT formatSRT = new FormatSRT();
                    srt = formatSRT.parseFile("sample.srt", stream);
                    break;
                case ASS:
                    FormatASS formatASS = new FormatASS();
                    srt = formatASS.parseFile("sample.ass", stream);
                    break;
                case SCC:
                    FormatSCC formatSCC = new FormatSCC();
                    srt = formatSCC.parseFile("sample.scc", stream);
                    break;
                case STL:
                    FormatSTL formatSTL = new FormatSTL();
                    srt = formatSTL.parseFile("sample.stl", stream);
                    break;
                case TTML:
                    FormatTTML formatTTML = new FormatTTML();
                    srt = formatTTML.parseFile("sample.ttml", stream);
                    break;
                case UNKNOWN:
                    break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FatalParsingException e) {
            e.printStackTrace();
        }
        return srt;
    }

    @Override
    protected void onPostExecute(TimedTextObject result) {
        if (null != srt) {
            Log.d("TAG", "subtitles loaded!!");
//            EventBus.getDefault().post(new VideoStatusEvent(VideoStatusEvent.READY_TO_START));
        }
        super.onPostExecute(result);
    }

//    private void parseDropBoxFile(){
//        int count;
//        InputStream is = null;
//        FileOutputStream fos = null;
//        try {
//            is = getDropBoxFile();
//            File f = getExternalFile();
//            fos = new FileOutputStream(f);
//            byte data[] = new byte[1024];
//            while ((count = is.read(data)) != -1) {
//                fos.write(data, 0, count);
//            }
//            InputStream stream = new FileInputStream(f);
//            FormatSRT formatSRT = new FormatSRT();
//            srt = formatSRT.parseFile("sample.srt", stream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                if(is != null && fos != null) {
//                    is.close();
//                    fos.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private InputStream getDropBoxFile(){
//        try {
//            DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
//            DbxClientV2 client = new DbxClientV2(config, DROP_BOX_ACCESS_TOKEN);
//            FullAccount account = null;
//
//            account = client.users().getCurrentAccount();
//
//            ListFolderResult result = client.files().listFolder("");
//            while (true) {
//                for (Metadata metadata : result.getEntries()) {
//                    System.out.println(metadata.getPathLower());
//                }
//
//                if (!result.getHasMore()) {
//                    break;
//                }
//
//                result = client.files().listFolderContinue(result.getCursor());
//            }
//            DbxDownloader<FileMetadata> downloader = client.files().download(result.getEntries().get(srtSource).getPathLower());
//            return downloader.getInputStream();
//        } catch (DbxException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    private File getExternalFile() {
//        File srt = null;
//        try {
//            srt = new File(context.getApplicationContext().getExternalFilesDir(null)
//                    .getPath() + "sample" + srtSource +".srt");
//            if(srt.exists()) srt.delete();
//            srt.createNewFile();
//            return srt;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}

