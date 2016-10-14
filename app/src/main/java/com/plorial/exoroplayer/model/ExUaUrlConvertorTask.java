package com.plorial.exoroplayer.model;


import android.os.AsyncTask;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;

import java.io.IOException;
/**
 * Created by plorial on 10/14/16.
 */

public class ExUaUrlConvertorTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        Connection connect = Jsoup.connect(url);
        try {
            connect.execute();
        } catch (UnsupportedMimeTypeException e) {
            url = e.getUrl();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }
}
