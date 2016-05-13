package com.plorial.exoroplayer.controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
                if(fragment.getVideoPath() == null){
                    Toast.makeText(fragment.getActivity(), R.string.video_path_null,Toast.LENGTH_LONG).show();
                }else {
                    startPlaying();
                }
                break;
        }
    }

    private void startPlaying() {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        FragmentManager manager = fragment.getActivity().getFragmentManager();
        bundle.putString(VideoFragment.VIDEO_PATH, fragment.getVideoPath());
        bundle.putString(VideoFragment.SRT1_PATH, fragment.getSub1Path());
        bundle.putString(VideoFragment.SRT2_PATH, fragment.getSub2Path());
        videoFragment.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, videoFragment);
        transaction.commit();
    }

    public void getFile() {
        FragmentManager manager = fragment.getActivity().getFragmentManager();
        EventBus.getDefault().post(fragment.getSaveInstanceStateBundle());
        FileChooseFragment fileChooseFragment = new FileChooseFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fileChooseFragment);
        transaction.commit();
    }
}
