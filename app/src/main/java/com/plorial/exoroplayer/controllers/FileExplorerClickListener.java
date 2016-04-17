package com.plorial.exoroplayer.controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.views.FileChooseFragment;
import com.plorial.exoroplayer.views.FileExplorerFragment;
import com.plorial.exoroplayer.views.VideoFragment;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by plorial on 4/17/16.
 */
public class FileExplorerClickListener implements View.OnClickListener {

    FileExplorerFragment fragment;

    public FileExplorerClickListener(FileExplorerFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBrowserVideoPath:
                FileExplorerFragment.bV = true;
                getFile();
                break;
            case R.id.bBrowserSub1Path:
                FileExplorerFragment.bS1 = true;
                getFile();
                break;
            case R.id.bBrowserSub2Path:
                FileExplorerFragment.bS2 = true;
                getFile();
                break;
            case R.id.bStartPlaying:
                startPlaying();
                break;
        }
    }

    private void startPlaying() {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        FragmentManager manager = fragment.getActivity().getFragmentManager();
        bundle.putString(VideoFragment.VIDEO_PATH, fragment.videoPath);
        bundle.putString(VideoFragment.SRT1_PATH, fragment.sub1Path);
        bundle.putString(VideoFragment.SRT2_PATH, fragment.sub2Path);
        videoFragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, videoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void getFile(){
        FragmentManager manager = fragment.getActivity().getFragmentManager();
        EventBus.getDefault().post(fragment.getSaveInstanceStateBundle());
        manager.putFragment(fragment.getSaveInstanceStateBundle(),FileExplorerFragment.CLASS_NAME,fragment);
        FileChooseFragment fileChooseFragment = new FileChooseFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fileChooseFragment);
        transaction.commit();
    }
}
