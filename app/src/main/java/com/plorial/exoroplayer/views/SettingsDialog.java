package com.plorial.exoroplayer.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.model.events.VideoStatusEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by plorial on 6/25/16.
 */
public class SettingsDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_dialog, null);
        final SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        final NumberPicker pickerSize = (NumberPicker) view.findViewById(R.id.pickerSubsSize);
        pickerSize.setMaxValue(40);
        pickerSize.setMinValue(6);
        pickerSize.setValue((int) preferences.getFloat("TEXT_SIZE",20));
        final NumberPicker pickerAlpha = (NumberPicker) view.findViewById(R.id.pickerSubsAlpha);
        pickerAlpha.setMaxValue(255);
        pickerAlpha.setMinValue(0);
        pickerAlpha.setValue(preferences.getInt("TEXT_ALPHA",185));
        final NumberPicker pickerTime = (NumberPicker) view.findViewById(R.id.pickerSubsTime);
        final int minValue = -50;
        final int maxValue = 50;
        pickerTime.setMaxValue(maxValue - minValue);
        pickerTime.setMinValue(0);
        pickerTime.setValue(VideoActivity.subsCorrector.get() - minValue);
        pickerTime.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int index) {
                return Integer.toString(index + minValue);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(R.string.settings).setNeutralButton(
                getActivity().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putInt("TEXT_ALPHA", pickerAlpha.getValue());
                        edit.putFloat("TEXT_SIZE", pickerSize.getValue());
                        edit.commit();
                        VideoActivity.subsCorrector.set(pickerTime.getValue() + minValue);
                        EventBus.getDefault().post(new VideoStatusEvent(VideoStatusEvent.READY_TO_START));
                        Activity activity = getActivity();
                        if (activity instanceof OnUpdateSettingsListener){
                            ((OnUpdateSettingsListener)activity).onUpdateSettings();
                        }
                    }
                });
        return builder.create();
    }
    public interface OnUpdateSettingsListener {
        void onUpdateSettings();
    }
}
