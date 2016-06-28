package com.plorial.exoroplayer.model;

import android.content.Context;
import android.graphics.Path;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by plorial on 6/28/16.
 */
public class DBValueEventListener implements ValueEventListener {

    public static final String TAG = DBValueEventListener.class.getSimpleName();

    private ArrayAdapter adapter;

    public DBValueEventListener(ArrayAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            adapter.clear();
            adapter.add("UP");
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                adapter.add(postSnapshot.getKey());
            }
        } else {
            //start video
            DownloadVideoUrlsTask task = new DownloadVideoUrlsTask(adapter.getContext());
            task.execute(new String[]{(String) dataSnapshot.getValue()});
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w(TAG, "onCancelled", databaseError.toException());
    }
}
