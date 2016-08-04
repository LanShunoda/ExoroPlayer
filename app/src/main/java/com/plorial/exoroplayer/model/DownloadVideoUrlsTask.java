package com.plorial.exoroplayer.model;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

/**
 * Created by plorial on 6/28/16.
 */
public class DownloadVideoUrlsTask implements Callable<File> {

    private String url;
    private Context context;

    public DownloadVideoUrlsTask(Context context, String url) {
        this.url = url;
        this.context = context;
    }


    @Override
    public File call() throws Exception {
        return downloadFromUrl(url, context);
    }

    public static File downloadFromUrl(String url, Context context){
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
        return file;
    }
}
