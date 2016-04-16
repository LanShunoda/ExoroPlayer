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

    private static final int REQUEST_PATH = 1;
    String curFileName;
    EditText edittext;
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.file_explorer_fragment, container, false);
        edittext = (EditText) view.findViewById(R.id.editText);
        button = (Button) view.findViewById(R.id.skipButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(v);
            }
        });
        if (getArguments() != null){
            curFileName = getArguments().getString("GetFileName");
            edittext.setText(curFileName);
        }
        return view;
    }



    public void getFile(View view){
        FileChooseFragment fileChooseFragment = new FileChooseFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fileChooseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
