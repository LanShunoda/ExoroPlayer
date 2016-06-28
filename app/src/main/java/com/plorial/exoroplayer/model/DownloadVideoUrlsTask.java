package com.plorial.exoroplayer.model;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.plorial.exoroplayer.views.VideoActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by plorial on 6/28/16.
 */
public class DownloadVideoUrlsTask extends AsyncTask<String,Void, String> {

    private final static String storageBucket = "gs://exoro-player.appspot.com";

    private Context context;
    private int numberOfUrl;

    public DownloadVideoUrlsTask(Context context, int numberOfUrl) {
        this.context = context;
        this.numberOfUrl = numberOfUrl;
    }

    @Override
    protected String doInBackground(String... url) {
        return getVideoUrls(url[0] ,context, numberOfUrl);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Intent intent = new Intent(context,VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEO_PATH, s);
        context.startActivity(intent);
    }

    private static String getVideoUrls(String url, Context context, int numberOfUrl){
        InputStream inputStream = null;
        OutputStream output = null;
        File file = null;
        try {
            File outputDir = context.getCacheDir();

            file = File.createTempFile("video_urls",".txt", outputDir);
            file.deleteOnExit();

            URL uri = new URL(url);
            URLConnection connection = uri.openConnection();

            inputStream = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int n = - 1;
            output = new FileOutputStream( file );
            while ( (n = inputStream.read(buffer)) != -1)
            {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            java.util.List<String> paths = readAllLines(file);
            String[] strings = paths.toArray(new String[paths.size()]);
            return strings[numberOfUrl];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> readAllLines(File file) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            List<String> result = new ArrayList<String>();
            for (;;) {
                String line = reader.readLine();
                if (line == null)
                    break;
                result.add(line);
            }
            return result;
        }finally {
            reader.close();
        }
    }
}
