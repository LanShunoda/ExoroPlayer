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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private int seasonNumber;
    private int episodeNumber;
    private String subRef;
    private Map<String, String> neededUrls;

    public VideoUrlsValueListener(Context context, int seasonNumber, int episodeNumber, String subRef) {
        this.context = context;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.subRef = subRef;
        neededUrls = new HashMap<>();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG, "count " + dataSnapshot.getChildrenCount());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ArrayList<Callable<File>> tasks = new ArrayList<>();
        List<String> qualities = new ArrayList<>();
        for (DataSnapshot url : dataSnapshot.getChildren()){
            qualities.add(url.getKey());
            DownloadVideoUrlsTask task = new DownloadVideoUrlsTask(context, (String) url.getValue());
            tasks.add(task);
        }
        try {
            List<Future<File>> completed = executor.invokeAll(tasks);
            int i = 0;
            for (Future<File> done : completed){
                List<String> lines = readAllLines(done.get());
                getNeededUrls(lines, seasonNumber, episodeNumber, qualities.get(i));
                i++;
            }
        } catch (InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry entry : neededUrls.entrySet()) {
            Log.d(TAG, entry.getKey() + ", " + entry.getValue());
        }
    }

    private void getNeededUrls(List<String> urls, int season, int episode, String quality){
        for (String url : urls){
            if(url.contains(".s" + stringOfNumber(season) + "e" + stringOfNumber(episode) + ".")){
                neededUrls.put(quality, url);
            }
        }
    }

    private static String stringOfNumber(int n){
        String result = "";
        if (n < 10){
            result = "0" + n;
        }else if (n > 9){
            result = String.valueOf(n);
        }
        return result;
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
