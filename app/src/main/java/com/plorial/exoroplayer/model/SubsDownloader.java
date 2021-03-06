package com.plorial.exoroplayer.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by plorial on 6/28/16.
 */
public class SubsDownloader extends AsyncTask<String, Void, File[]> {

    private static final String TAG = SubsDownloader.class.getSimpleName();

    private Context context;

    public SubsDownloader(Context context) {
        this.context = context;
    }

    @Override
    protected File[] doInBackground(String... lists) {
        File file = DownloadVideoUrlsTask.downloadFromUrl(lists[0], context);
        file.deleteOnExit();
        return unzipFile(file.getAbsolutePath(), context.getCacheDir() + File.separator + file.getName().substring(file.getName().length()-5,file.getName().length()-1));
    }

    private File[] unzipFile(String zipFile, String outputFolder){

        byte[] buffer = new byte[1024];

        ZipInputStream zis = null;
        FileOutputStream fos = null;

        try{
            File folder = new File(outputFolder);
            folder.deleteOnExit();
            if(!folder.exists()){
                folder.mkdir();
            }
            zis = new ZipInputStream(new FileInputStream(zipFile));

            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder, fileName);
                newFile.deleteOnExit();

                new File(newFile.getParent()).mkdirs();

                fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                ze = zis.getNextEntry();
            }
            Log.e(TAG, Arrays.toString(folder.listFiles()));
            return folder.listFiles();
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (zis != null) {
                    zis.closeEntry();
                    zis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
