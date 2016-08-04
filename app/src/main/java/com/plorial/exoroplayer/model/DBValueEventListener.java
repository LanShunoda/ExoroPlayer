package com.plorial.exoroplayer.model;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
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
    private ListView listView;
    private String videoUrls;

    public DBValueEventListener(ArrayAdapter adapter, View view, ListView listView) {
        this.adapter = adapter;
        this.view = view;
        this.listView = listView;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            if (dataSnapshot.hasChild("zzz_video_urls")) {
                videoUrls = (String) dataSnapshot.child("zzz_video_urls").getValue();
            }
            adapter.clear();
            adapter.add(view.getContext().getString(R.string.up));
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                if (postSnapshot.getKey().equals("zzz_video_urls") || postSnapshot.getKey().equals("zzz_urls")) {
                } else {
                    adapter.add(postSnapshot.getKey());
                }
            }

        } else {
            String key = dataSnapshot.getKey();
            if(key.equals("UP")){
                Toast.makeText(adapter.getContext(),"Root directory", Toast.LENGTH_SHORT).show();
            } else {
                //start video
                String subRef = (String) dataSnapshot.getValue();
                int n = Integer.parseInt(key.substring(8)) - 1;
                dataSnapshot.getRef().getParent().child("zzz_urls").addListenerForSingleValueEvent(new VideoUrlsValueListener());
//                DownloadVideoUrlsTask task = new DownloadVideoUrlsTask(adapter.getContext(), n, subRef);
//                view.findViewById(R.id.bar_preparing).setVisibility(View.VISIBLE);
//                task.execute(videoUrls);
            }
        }
        listView.setEnabled(true);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        FirebaseCrash.log(databaseError.getMessage());
        Log.w(TAG, "onCancelled", databaseError.toException());
        Toast.makeText(adapter.getContext(),databaseError.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
