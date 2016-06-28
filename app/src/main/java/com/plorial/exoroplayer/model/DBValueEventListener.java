package com.plorial.exoroplayer.model;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

/**
 * Created by plorial on 6/28/16.
 */
public class DBValueEventListener implements ValueEventListener {

    public static final String TAG = DBValueEventListener.class.getSimpleName();

    private ArrayAdapter adapter;
    private String videoUrls;

    public DBValueEventListener(ArrayAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            if (dataSnapshot.hasChild("video_urls")){
                videoUrls = (String) dataSnapshot.child("video_urls").getValue();
            }
            adapter.clear();
            adapter.add("UP");
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                adapter.add(postSnapshot.getKey());
            }
        } else {
            String key = dataSnapshot.getKey();
            if(key.equals("video_urls")){

            }else {
                //start video
                String subRef = (String) dataSnapshot.getValue();
                int n = Integer.parseInt(key.substring(5)) - 1;
                DownloadVideoUrlsTask task = new DownloadVideoUrlsTask(adapter.getContext(), n);
                task.execute(videoUrls);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w(TAG, "onCancelled", databaseError.toException());
    }
}
