package com.plorial.exoroplayer.views;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.model.Item;

public class MainActivity extends AppCompatActivity implements FileChooseFragment.OnFileSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FileExplorerFragment fileExplorerFragment = new FileExplorerFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fileExplorerFragment);
        transaction.addToBackStack(null);
        transaction.commit();

//        VideoFragment videoFragment = new VideoFragment();
//        videoFragment.setRetainInstance(true);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment_container, videoFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
    }

    @Override
    public void onFileSelected(Item o) {

        Bundle bundle = new Bundle();
        bundle.putString("GetPath",o.getPath());
        bundle.putString("GetFileName",o.getName());
        FileExplorerFragment fileExplorerFragment = new FileExplorerFragment();
        fileExplorerFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fileExplorerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
