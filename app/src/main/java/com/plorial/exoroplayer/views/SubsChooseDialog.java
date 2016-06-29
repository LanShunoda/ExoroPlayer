package com.plorial.exoroplayer.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.plorial.exoroplayer.R;

import java.io.File;

/**
 * Created by plorial on 6/29/16.
 */
public class SubsChooseDialog extends DialogFragment {

    File[] subs;
    ArrayAdapter<String> adapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.subs_choose_dialog,null);
        ListView listView = (ListView) view.findViewById(R.id.subsChooseListView);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        VideoActivity activity = (VideoActivity) getActivity();
        subs = activity.subs;
        fillAdapter();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle("Choose Subtitles");

        return builder.create();
    }

    private void fillAdapter(){
        adapter.add(String.valueOf(subs.length));
        for (int i = 0; i < subs.length; i++){
            adapter.add(subs[i].getName());
        }
    }
}
