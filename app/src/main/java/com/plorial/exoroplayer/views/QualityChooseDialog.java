package com.plorial.exoroplayer.views;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.model.events.CancelQualitySelectingEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by plorial on 8/6/16.
 */
public class QualityChooseDialog extends DialogFragment {

    private static final String TAG = QualityChooseDialog.class.getSimpleName();

    private TreeMap<String, String> urls;
    private String subRef;
    private int checked;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            subRef = savedInstanceState.getString("SUB_REF");
            urls = (TreeMap<String, String>) savedInstanceState.getSerializable("URLS_MAP");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.video_quality_choose);
        checked = 0;
        CharSequence[] qualities = new CharSequence[urls.navigableKeySet().size()];
        builder.setSingleChoiceItems(urls.navigableKeySet().toArray(qualities), checked, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checked = i;
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        });
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Object[] url = urls.values().toArray();
                if(url != null && url.length != 0) {
                    startVideoActivity((String) url[checked]);
                }else {
                    dismiss();
                    Toast.makeText(getActivity(),getActivity().getString(R.string.click_up),Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNeutralButton(getActivity().getString(R.string.download), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addDownload((String) urls.values().toArray()[checked]);
            }
        });
        getActivity().findViewById(R.id.bar_preparing).setVisibility(View.INVISIBLE);
        return builder.create();
    }

    private void startVideoActivity(String videoUrl){
        Intent intent = new Intent(getActivity(),VideoActivity.class);
        intent.putExtra(VideoActivity.VIDEO_PATH, videoUrl);
        intent.putExtra(VideoActivity.SUB_REF, subRef);
        getActivity().startActivity(intent);
    }

    public void setUrls(TreeMap<String, String> urls) {
        this.urls = urls;
    }

    public void setSubRef(String subRef) {
        this.subRef = subRef;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        ListView lv = null;
        if(activity != null)
        lv = (ListView) activity.findViewById(R.id.listView);
        if(lv != null){
            lv.performItemClick(lv.getChildAt(0),
                    0, lv.getAdapter().getItemId(0));
        }else {
            EventBus.getDefault().post(new CancelQualitySelectingEvent());
        }
    }

    private void addDownload(String url){
        DownloadManager downloadManager = (DownloadManager) getActivity()
                .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request downloadReq = new DownloadManager.Request(
                Uri.parse(url));
        downloadManager.enqueue(downloadReq);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("URLS_MAP", urls);
        outState.putString("SUB_REF", subRef);
    }
}
