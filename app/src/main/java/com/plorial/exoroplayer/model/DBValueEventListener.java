package com.plorial.exoroplayer.model;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            adapter.clear();
            adapter.add("UP");
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                adapter.add(postSnapshot.getKey());
            }
        } else {
            //start video
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w(TAG, "onCancelled", databaseError.toException());
    }
}
