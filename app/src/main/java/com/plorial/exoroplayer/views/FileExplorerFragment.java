package com.plorial.exoroplayer.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.controllers.FileExplorerClickListener;

/**
 * Created by plorial on 4/16/16.
 */
public class FileExplorerFragment extends Fragment {

    public static final String FILE_PATH = "GetFilePath";
    public static final String CLASS_NAME = FileExplorerFragment.class.getSimpleName();

    private EditText etVideoPath;
    public static boolean bV = false;

    private TextView etSub1Path;
    public static boolean bS1 = false;

    private TextView etSub2Path;
    public static boolean bS2 = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.file_explorer_fragment, container, false);
        initViews(view);
        if (savedInstanceState != null) {
            Bundle bundle = savedInstanceState.getBundle(this.getClass().getSimpleName());
            restoreState(bundle);
        }
        if (getArguments() != null){
            restoreState(getArguments().getBundle(CLASS_NAME));
            if(bV) {
                etVideoPath.setText(getArguments().getString(FILE_PATH));
                bV = false;
            }else if (bS1){
                etSub1Path.setText(getArguments().getString(FILE_PATH));
                bS1 = false;
            }else if(bS2){
                etSub2Path.setText(getArguments().getString(FILE_PATH));
                bS2 = false;
            }
        }
        return view;
    }

    private void restoreState(Bundle bundle) {
        etVideoPath.setText(bundle.getString("VIDEO_PATH"));
        etSub1Path.setText(bundle.getString("SUB1_PATH"));
        etSub2Path.setText(bundle.getString("SUB2_PATH"));
    }

    private void initViews(View view) {
        etVideoPath = (EditText) view.findViewById(R.id.etVideoPath);
        etSub1Path = (TextView) view.findViewById(R.id.etSub1Path);
        etSub2Path = (TextView) view.findViewById(R.id.etSub2Path);
        FileExplorerClickListener listener = new FileExplorerClickListener(this);
        Button bVideo = (Button) view.findViewById(R.id.bBrowserVideoPath);
        Button bSub1 = (Button) view.findViewById(R.id.bBrowserSub1Path);
        Button bSub2 = (Button) view.findViewById(R.id.bBrowserSub2Path);
        Button bPlay = (Button) view.findViewById(R.id.bStartPlaying);
        bVideo.setOnClickListener(listener);
        bSub1.setOnClickListener(listener);
        bSub2.setOnClickListener(listener);
        bPlay.setOnClickListener(listener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(this.getClass().getSimpleName(),getSaveInstanceStateBundle());
    }

    public Bundle getSaveInstanceStateBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("VIDEO_PATH",etVideoPath.getText().toString());
        bundle.putString("SUB1_PATH", etSub1Path.getText().toString());
        bundle.putString("SUB2_PATH", etSub2Path.getText().toString());
        return bundle;
    }

    public String getVideoPath(){
        String result = etVideoPath.getText().toString();
        if (result.equals("")){
            result = null;
        }
        return result;
    }

    public String getSub1Path(){
        String result = etSub1Path.getText().toString();
        if (result.equals("")){
            result = null;
        }
        return result;
    }

    public String getSub2Path(){
        String result = etSub2Path.getText().toString();
        if (result.equals("")){
            result = null;
        }
        return result;
    }
}
