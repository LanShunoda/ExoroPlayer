package com.plorial.exoroplayer.views;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.model.Item;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements FileChooseFragment.OnFileSelectedListener{

    public Bundle fileExplorerBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            FileExplorerFragment fileExplorerFragment = new FileExplorerFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, fileExplorerFragment);
            transaction.addToBackStack(FileExplorerFragment.CLASS_NAME);
            transaction.commit();
        }
    }

    @Override
    public void onFileSelected(Item o) {

        Bundle bundle = new Bundle();
        bundle.putString(FileExplorerFragment.FILE_PATH,o.getPath());
        bundle.putString(FileExplorerFragment.FILE_NAME,o.getName());
        bundle.putBundle(FileExplorerFragment.CLASS_NAME, fileExplorerBundle);
        FileExplorerFragment fragment = new FileExplorerFragment();

        fragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(FileExplorerFragment.CLASS_NAME);
        transaction.commit();
    }

    @Subscribe
    public void onBundleEvent(Bundle bundle){
        fileExplorerBundle = bundle;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
