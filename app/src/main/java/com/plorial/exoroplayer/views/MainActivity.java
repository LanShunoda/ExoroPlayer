package com.plorial.exoroplayer.views;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.plorial.exoroplayer.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setRetainInstance(true);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, videoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
