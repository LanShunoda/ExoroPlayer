package com.plorial.exoro.model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.plorial.exoro.R;
import com.plorial.exoro.views.VideoActivity;

/**
 * Created by plorial on 6/28/16.
 */
public class DBValueEventListener implements ValueEventListener {

    public static final String TAG = DBValueEventListener.class.getSimpleName();

    private ArrayAdapter adapter;
    private View view;
    private Activity activity;
    private ListView listView;
    private String videoUrl;
    private String srt;

    public DBValueEventListener(ArrayAdapter adapter, View view, Activity activity) {
        this.adapter = adapter;
        this.view = view;
        this.activity = activity;
        this.listView = (ListView) view.findViewById(R.id.listView);
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            if (dataSnapshot.hasChild("id")) {
                videoUrl = (String) dataSnapshot.child("id").getValue();
                if(dataSnapshot.hasChild("srt")){
                    srt = (String) dataSnapshot.child("srt").getValue();
                }
                startVideoActivity(videoUrl,srt);
            }
            adapter.clear();
            adapter.add(view.getContext().getString(R.string.up));
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                if (postSnapshot.getKey().equals("id") || postSnapshot.getKey().equals("srt")) {
                } else {
                    adapter.add(postSnapshot.getKey());
                }
            }
        }
        listView.setEnabled(true);
   }

    private void startVideoActivity(String videoUrl, String subRef){
        Intent intent = new Intent(activity,VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEO_PATH, videoUrl);
        intent.putExtra(VideoActivity.SUB_REF, subRef);
        activity.startActivity(intent);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.log(databaseError.getMessage());
        Log.w(TAG, "onCancelled", databaseError.toException());
        Toast.makeText(adapter.getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
