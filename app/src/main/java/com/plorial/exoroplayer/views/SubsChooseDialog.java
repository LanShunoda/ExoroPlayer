package com.plorial.exoroplayer.views;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.plorial.exoroplayer.R;

import java.io.File;

/**
 * Created by plorial on 6/29/16.
 */
public class SubsChooseDialog extends DialogFragment {

    File[] subs;
    String[] fileNames;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final VideoActivity activity = (VideoActivity) getActivity();
        subs = activity.subs;
        fileNames = new String[subs.length];
        fillArray();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_subtitles);
        builder.setMultiChoiceItems(fileNames,activity.checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                activity.checkedItems[i] = b;
            }
        });
        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.updateSubs();
            }
        });
        return builder.create();
    }

    private void fillArray(){
        for (int i = 0; i < fileNames.length; i++){
            fileNames[i] = subs[i].getName();
        }
    }
}
