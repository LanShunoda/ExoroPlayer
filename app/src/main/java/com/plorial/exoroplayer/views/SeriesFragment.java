package com.plorial.exoroplayer.views;

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
import com.google.firebase.database.ValueEventListener;
import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.controllers.SeriesClickListener;
import com.plorial.exoroplayer.model.DBValueEventListenerForEx;
import com.plorial.exoroplayer.model.DBValueEventListenerForFS;
import com.plorial.exoroplayer.model.events.CancelQualitySelectingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by plorial on 6/27/16.
 */
public class SeriesFragment extends Fragment {

    private static final String TAG = SeriesFragment.class.getSimpleName();

    public static final String DB_SOURCE = "DB_SOURCE";
    public static final String EX = "EX";
    public static final String FS = "FS";
    public static final String CONTENT = "CONTENT";
    public static final String SERIES = "SERIES";
    public static final String FILMS = "FILMS";
    public static final String ANIME = "ANIME";

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private DatabaseReference dbRef;
    private FirebaseDatabase firebaseDatabase;
    private ValueEventListener dbValueEventListener;

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
        Bundle args = getArguments();
        if(savedInstanceState != null){
            args = savedInstanceState;
        }
        if(args != null && args.getString(DB_SOURCE).equals(EX)){
            switch (args.getString(CONTENT)){
                case SERIES:
                    dbRef = firebaseDatabase.getReference("Series EX");
                    break;
                case ANIME:
                    dbRef = firebaseDatabase.getReference("Anime");
                    break;
                case FILMS:
                    dbRef = firebaseDatabase.getReference("Films");
                    break;
                default:
                    dbRef = firebaseDatabase.getReference("Series EX");
            }
            dbValueEventListener = new DBValueEventListenerForEx(adapter,view, getActivity());
        }else if(args != null && args.getString(DB_SOURCE).equals(FS)){
            dbRef = firebaseDatabase.getReference("Series FS");
            dbValueEventListener = new DBValueEventListenerForFS(adapter,view, getActivity());
        }
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
        if(getArguments() != null){
            outState.putString(DB_SOURCE, getArguments().getString(DB_SOURCE));
        }
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
