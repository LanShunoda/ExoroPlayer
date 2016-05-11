package com.plorial.exoroplayer.views;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.devbrackets.android.exomedia.util.MediaUtil;
import com.plorial.exoroplayer.R;
import com.plorial.exoroplayer.model.FileArrayAdapter;
import com.plorial.exoroplayer.model.Item;
import com.plorial.exoroplayer.model.SubtitlesUtil;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by plorial on 4/16/16.
 */
public class FileChooseFragment extends ListFragment {
    private File currentDir;
    private FileArrayAdapter adapter;
    OnFileSelectedListener mCallback;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentDir = new File(Environment.getExternalStorageDirectory(),"");
        fill(currentDir);
    }

    private void fill(File f) {
        File[]dirs = f.listFiles();
//        getActivity().setTitle("Current Dir: "+f.getName());
        Log.d(getClass().getSimpleName(),"Current Dir: "+f.getPath());
        List<Item> dir = new ArrayList<Item>();
        List<Item>fls = new ArrayList<Item>();
        try {
            for (File ff : dirs) {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if (ff.isDirectory()) {
                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if (fbuf != null) {
                        buf = fbuf.length;
                    } else buf = 0;
                    String num_item = String.valueOf(buf);
                    if (buf == 0) num_item = num_item + " item";
                    else num_item = num_item + " items";

                    //String formated = lastModDate.toString();
                    dir.add(new Item(ff.getName(), num_item, date_modify, ff.getAbsolutePath(), "directory_icon"));
                } else {
                    MediaUtil.MediaType type = MediaUtil.getMediaType(ff.getAbsolutePath());
                    SubtitlesUtil.SubtitlesType subtitlesType = SubtitlesUtil.getSubtitlesType(ff.getAbsolutePath());
                    if (type.equals(MediaUtil.MediaType.UNKNOWN)) {
                        if (subtitlesType.equals(SubtitlesUtil.SubtitlesType.UNKNOWN)) {
                            fls.add(new Item(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "file_icon"));
                        }else {
                            fls.add(new Item(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "subtitles"));
                        }
                    }else {
                        fls.add(new Item(ff.getName(), ff.length() + " Byte", date_modify, ff.getAbsolutePath(), "video"));
                    }
                }
            }
        }catch (Exception e) {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new Item("..","Parent Directory","",f.getParent(),"directory_up"));
        adapter = new FileArrayAdapter(getActivity(),R.layout.file_view_item,dir);
        this.setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Item o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
            switch (o.getImage()){
                case "directory_icon":
                    currentDir = new File(o.getPath());
                    break;
                case "directory_up":
                    if (o.getPath() == null) {
                        return;
                    }else {
                        currentDir = new File(o.getPath());
                    }
            }
            fill(currentDir);
        }
        else {
            onFileClick(o);
        }
    }

    private void onFileClick(Item o) {
        mCallback.onFileSelected(o);
    }

    public interface OnFileSelectedListener {
        public void onFileSelected(Item o);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnFileSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFileSelectedListener");
        }
    }
}
