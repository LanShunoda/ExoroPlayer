package com.plorial.exoroplayer.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.controllers.SeriesClickListener;
import com.plorial.exoroplayer.model.DBValueEventListener;

/**
 * Created by plorial on 6/27/16.
 */
public class SeriesFragment extends Fragment {

    private static final String TAG = SeriesFragment.class.getSimpleName();

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private DatabaseReference dbRef;
    private FirebaseDatabase firebaseDatabase;
    private DBValueEventListener dbValueEventListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference("Series");
        dbValueEventListener = new DBValueEventListener(adapter);
        listView.setOnItemClickListener(new SeriesClickListener(dbRef, this));
        getDBFiles(dbRef);
    }

    public void getDBFiles(DatabaseReference reference) {
        reference.addListenerForSingleValueEvent(dbValueEventListener);
    }
}
