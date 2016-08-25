package com.plorial.exoroplayer.model;

import android.app.Activity;
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
    private Activity activity;
    private ListView listView;
    private String videoUrls;
    private int seasonNumber;

    public DBValueEventListener(ArrayAdapter adapter, View view, Activity activity) {
        this.adapter = adapter;
        this.view = view;
        this.activity = activity;
        this.listView = (ListView) view.findViewById(R.id.listView);
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        if (dataSnapshot.hasChildren()) {
            if (dataSnapshot.hasChild("zzz_video_urls")) {
                videoUrls = (String) dataSnapshot.child("zzz_video_urls").getValue();
            }
            if(!dataSnapshot.getRef().equals(dataSnapshot.getRef().getRoot())) {
                if (dataSnapshot.getKey().contains("Season")) {
                    seasonNumber = Integer.parseInt(dataSnapshot.getKey().substring(7));
                }
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
            view.findViewById(R.id.bar_preparing).setVisibility(View.VISIBLE);
            String subRef = (String) dataSnapshot.getValue();
            int n = Integer.parseInt(key.substring(8));
            dataSnapshot.getRef().getParent().child("zzz_urls").addListenerForSingleValueEvent(new VideoUrlsValueListener(activity, seasonNumber, n, subRef));
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
