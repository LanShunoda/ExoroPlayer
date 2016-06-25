package com.plorial.exoroplayer.controllers;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.views.FileChooseFragment;
import com.plorial.exoroplayer.views.FileExplorerFragment;
import com.plorial.exoroplayer.views.VideoActivity;

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
        Intent intent = new Intent(fragment.getActivity(),VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEO_PATH, fragment.getVideoPath());
        intent.putExtra(VideoActivity.SRT1_PATH, fragment.getSub1Path());
        intent.putExtra(VideoActivity.SRT2_PATH, fragment.getSub2Path());
        fragment.getActivity().startActivity(intent);
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
