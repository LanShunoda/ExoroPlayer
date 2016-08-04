package com.plorial.exoroplayer.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.plorial.exoroplayer.views.VideoActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by plorial on 8/4/16.
 */
public class VideoUrlsValueListener implements ValueEventListener {

    private static final String TAG = VideoUrlsValueListener.class.getSimpleName();

    private Context context;
    private int episodeNumber;
    private String subRef;

    public VideoUrlsValueListener(Context context, int episodeNumber, String subRef) {

        this.context = context;
        this.episodeNumber = episodeNumber;
        this.subRef = subRef;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG, "count " + dataSnapshot.getChildrenCount());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ArrayList<Callable<File>> tasks = new ArrayList<>();
        for (DataSnapshot url : dataSnapshot.getChildren()){
            Log.d(TAG, "URL key: " + url.getKey() + " url value: " + url.getValue());
            DownloadVideoUrlsTask task = new DownloadVideoUrlsTask(context, (String) url.getValue());
            tasks.add(task);
        }
        try {
            List<Future<File>> completed = executor.invokeAll(tasks);
            for (Future<File> done : completed){
                List<String> lines = readAllLines(done.get());
                Log.d(TAG, "file urls");
                Log.d(TAG, lines.toString());
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.log(databaseError.getMessage());
    }

    private void startVideoActivity(String videoUrl){
        Intent intent = new Intent(context,VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEO_PATH, videoUrl);
        intent.putExtra(VideoActivity.SUB_REF, subRef);
        context.startActivity(intent);
    }

    private static String getNeededLine(File file, int number){
        try {
            java.util.List<String> paths = readAllLines(file);
            String[] strings = paths.toArray(new String[paths.size()]);
            return strings[number];
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
