package com.plorial.exoroplayer.views;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.plorial.exoroplayer.R;

/**
 * Created by plorial on 4/16/16.
 */
public class FileExplorerFragment extends Fragment {

    public static final String FILE_NAME = "GetFileName";
    public static final String FILE_PATH = "GetFilePath";
    private String curFileName;
    private EditText edittext;
    private Button bBrowser;
    private Button bPlay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_explorer_fragment, container, false);
        edittext = (EditText) view.findViewById(R.id.editText);
        bBrowser = (Button) view.findViewById(R.id.skipButton);
        bBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(v);
            }
        });
        if (getArguments() != null){
            curFileName = getArguments().getString(FILE_NAME);
            edittext.setText(curFileName);
        }
        bPlay = (Button) view.findViewById(R.id.bStartPlaying);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaying();
            }
        });
        return view;
    }

    private void startPlaying() {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(FileExplorerFragment.FILE_PATH, getArguments().getString(FILE_PATH));
        bundle.putString(FileExplorerFragment.FILE_NAME, getArguments().getString(FILE_NAME));
        videoFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, videoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void getFile(View view){
        FileChooseFragment fileChooseFragment = new FileChooseFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fileChooseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
