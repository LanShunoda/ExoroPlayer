package com.plorial.exoro.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plorial.exoro.BuildConfig;
import com.plorial.exoro.R;

/**
 * Created by plorial on 7/13/16.
 */
public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView version = (TextView) view.findViewById(R.id.tvVersion);
        version.setText("v. " + BuildConfig.VERSION_NAME);
    }
}
