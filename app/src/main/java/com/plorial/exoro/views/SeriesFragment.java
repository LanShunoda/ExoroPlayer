package com.plorial.exoro.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.plorial.exoro.R;
import com.plorial.exoro.controllers.SeriesClickListener;
import com.plorial.exoro.model.DBValueEventListener;
import com.plorial.exoro.model.events.CancelQualitySelectingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
        view.findViewById(R.id.bar_preparing).setVisibility(View.INVISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        if(savedInstanceState != null){
            Log.d(TAG, "db ref " + savedInstanceState.getString("DB_REF"));
            dbRef = firebaseDatabase.getReferenceFromUrl(savedInstanceState.getString("DB_REF"));
        }else {
            dbRef = firebaseDatabase.getReference("Series");
        }
        dbValueEventListener = new DBValueEventListener(adapter,view, getActivity());
        listView.setOnItemClickListener(new SeriesClickListener(dbRef, this));
        getDBFiles(dbRef);
    }

    public void getDBFiles(DatabaseReference reference) {
        dbRef = reference;
        getActivity().setTitle(reference.getKey());
        listView.setEnabled(false);
        reference.addListenerForSingleValueEvent(dbValueEventListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("DB_REF", dbRef.getRoot().toString());
    }

    @Subscribe
    public void onCancelQualitySelectingEvent(CancelQualitySelectingEvent event){
        listView.performItemClick(listView.getChildAt(0),
                0, listView.getAdapter().getItemId(0));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }
}
