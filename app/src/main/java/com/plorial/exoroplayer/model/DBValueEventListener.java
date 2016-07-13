package com.plorial.exoroplayer.model;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.plorial.exoroplayer.R;

/**
 * Created by plorial on 6/28/16.
 */
public class DBValueEventListener implements ValueEventListener {

    public static final String TAG = DBValueEventListener.class.getSimpleName();

    private ArrayAdapter adapter;
    private View view;
    private String videoUrls;

    public DBValueEventListener(ArrayAdapter adapter, View view) {
        this.adapter = adapter;
        this.view = view;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            if (dataSnapshot.hasChild("zzz_video_urls")){
                videoUrls = (String) dataSnapshot.child("zzz_video_urls").getValue();
            }
            adapter.clear();
            adapter.add(view.getContext().getString(R.string.up));
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                if(postSnapshot.getKey().equals("zzz_video_urls")){}
                else {
                    adapter.add(postSnapshot.getKey());
                }
            }
        } else {
            String key = dataSnapshot.getKey();
            if(key.equals("zzz_video_urls")){
                Toast.makeText(adapter.getContext(),"Don't click here", Toast.LENGTH_SHORT);
            }else if(key.equals("UP")){
                Toast.makeText(adapter.getContext(),"Root directory", Toast.LENGTH_SHORT);
            }else {
                //start video
                String subRef = (String) dataSnapshot.getValue();
                int n = Integer.parseInt(key.substring(8)) - 1;
                DownloadVideoUrlsTask task = new DownloadVideoUrlsTask(adapter.getContext(), n, subRef);
                view.findViewById(R.id.bar_preparing).setVisibility(View.VISIBLE);
                task.execute(videoUrls);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w(TAG, "onCancelled", databaseError.toException());
        Toast.makeText(adapter.getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT);
    }
}
