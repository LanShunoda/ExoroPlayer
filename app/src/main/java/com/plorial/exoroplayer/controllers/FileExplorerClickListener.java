package com.plorial.exoroplayer.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.views.FileChooseFragment;
import com.plorial.exoroplayer.views.FileExplorerFragment;
import com.plorial.exoroplayer.views.NavigationDrawerActivity;
import com.plorial.exoroplayer.views.VideoActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by plorial on 4/17/16.
 */
public class FileExplorerClickListener implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    public final static String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public final static int RC_PERMISSIONS = 12345;

    FileExplorerFragment fragment;

    public FileExplorerClickListener(FileExplorerFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBrowserVideoPath:
                checkPermission();
                FileExplorerFragment.bV = true;
                break;
            case R.id.bBrowserSub1Path:
                checkPermission();
                FileExplorerFragment.bS1 = true;
                break;
            case R.id.bBrowserSub2Path:
                checkPermission();
                FileExplorerFragment.bS2 = true;
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

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!EasyPermissions.hasPermissions(fragment.getContext(), PERMISSIONS)){
                EasyPermissions.requestPermissions(fragment.getContext(), fragment.getActivity().getString(R.string.rt_permission_message), RC_PERMISSIONS, PERMISSIONS);
            } else {
                getFile();
            }
        } else {
            getFile();
        }
    }

    private void startPlaying() {
        Activity activity = fragment.getActivity();
        if(activity instanceof NavigationDrawerActivity){
            FirebaseAnalytics analytics = ((NavigationDrawerActivity)activity).firebaseAnalytics;
            Bundle analyticsBundle = new Bundle();
            analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME,"Local file started");
            analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, analyticsBundle);
        }
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        getFile();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(fragment.getActivity(),"You cant open local file without permission", Toast.LENGTH_LONG).show();
        FileExplorerFragment.bS1 = false;
        FileExplorerFragment.bS2 = false;
        FileExplorerFragment.bV = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, fragment.getActivity());
    }
}
