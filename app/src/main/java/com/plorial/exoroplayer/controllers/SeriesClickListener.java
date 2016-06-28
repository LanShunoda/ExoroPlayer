package com.plorial.exoroplayer.controllers;

import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseReference;
import com.plorial.exoroplayer.views.SeriesFragment;

/**
 * Created by plorial on 6/28/16.
 */
public class SeriesClickListener implements AdapterView.OnItemClickListener {

    private DatabaseReference dbRef;
    private SeriesFragment fragment;

    public SeriesClickListener(DatabaseReference dbRef, SeriesFragment fragment) {
        this.dbRef = dbRef;
        this.fragment = fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String ref = (String) adapterView.getItemAtPosition(i);
        if (ref.equals("UP")){
            dbRef = dbRef.getParent();
        }else {
            dbRef = dbRef.child(ref);
        }
        fragment.getDBFiles(dbRef);
    }
}
