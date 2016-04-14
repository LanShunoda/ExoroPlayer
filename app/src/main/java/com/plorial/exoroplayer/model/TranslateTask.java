package com.plorial.exoroplayer.model;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by plorial on 4/14/16.
 */
public class TranslateTask extends AsyncTask<String, Void, String> {

    private static final String URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";
    private static final String KEY = "trnsl.1.1.20160414T144825Z.330d06e1d4fe2445.f5e6425cf911b5d7ec54a2f85c770d3553467d35";

    @Override
    protected String doInBackground(String... params) {
        String translated = "";
        String urlStr = URL + KEY;
        String word = params[0];
        String lang = "ru";
        try {
            URL urlObj = null;

            urlObj = new URL(urlStr);

            HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("text=" + URLEncoder.encode(word, "UTF-8") + "&lang=" + lang);

            InputStream response = connection.getInputStream();
            String json = new java.util.Scanner(response).nextLine();
            int start = json.indexOf("[");
            int end = json.indexOf("]");
            translated = json.substring(start + 2, end - 1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return translated;
    }

}
