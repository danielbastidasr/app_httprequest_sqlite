package com.android.dani.who_wants_to_be_millionaire_.Bussiness;

import android.os.AsyncTask;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by dabasra on 24/05/2015.
 */
public class SaveScore  extends AsyncTask<String, Void, Void> {
    private static String urlPlay = "http://wwtbamandroid.appspot.com/rest/highscores/adm";
    private  String PUT_PARAMS;

    @Override
    protected Void doInBackground(String... params) {
        PUT_PARAMS="name="+params[0]+"&"+"score="+params[1];
        URL url=null;
        try {
            url = new URL(urlPlay);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection client=null;
        try {

            client = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.setRequestMethod("PUT");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.setDoOutput(true);
        client.setDoInput(true);
        try {
            OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream(),Charset.forName("UTF-8"));
            wr.write(PUT_PARAMS);
            wr.close();
            InputStream r=client.getInputStream();
            r.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
