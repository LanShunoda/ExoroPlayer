package com.plorial.exoroplayer.model;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by plorial on 8/4/16.
 */
public class VideoUrlsValueListener implements ValueEventListener {

    private static final String TAG = VideoUrlsValueListener.class.getSimpleName();

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d(TAG, "count " + dataSnapshot.getChildrenCount());
        for (DataSnapshot url : dataSnapshot.getChildren()){
            Log.d(TAG, "URL key: " + url.getKey() + " url value: " + url.getValue());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.log(databaseError.getMessage());
    }
}
