package com.android.dani.who_wants_to_be_millionaire_.Bussiness;

import android.os.AsyncTask;


import com.android.dani.who_wants_to_be_millionaire_.Data.HighScoreList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;


/**
 * Created by dabasra on 24/05/2015.
 */
public class SaveFriendWeb extends AsyncTask<String, Void, Void> {

    private static String urlScoreFriends="http://wwtbamandroid.appspot.com/rest/highscores?name=Unnamed";
    private static String urlSettings = "http://wwtbamandroid.appspot.com/rest/friends";
    private  String POST_PARAMS;

    private HighScoreList scoreFriendList;
    private List<HighScore> lh;

    private boolean exist;

    @Override
    protected Void doInBackground(String... params) {

        exist=false;
        POST_PARAMS="name="+params[0]+"&"+"friend_name="+params[1];
        getFriends();
        //if the friend _ name is already in the website we don't have to save it again
        for(int i=0; i< lh.size(); i++ ){
             if(lh.get(i).getName().equals(params[1] )) {
                return null;
            }
        }
            URL url = null;
            try {
                url = new URL(urlSettings);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection client = null;
            try {
                client = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                client.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream(), Charset.forName("UTF-8"));
                wr.write(POST_PARAMS);
                wr.flush();
                wr.close();
                InputStream r = client.getInputStream();
                r.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }


    public void getFriends(){
        URL url=null;
        try {
            url = new URL(urlScoreFriends);
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
            client.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        try {
            client.getResponseCode();
            //Log.d("ESTA ES LA RESPUESTA", "----------------doInBackground -------------: " + client.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(client.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        scoreFriendList= new HighScoreList();
        scoreFriendList.setScores(gson.fromJson(reader, HighScoreList.class).getScores());
        //Log.d("ESTO ES LA RESPUESTA", "----doInBackground ---" + scoreFriendList.getScores().toString());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lh=scoreFriendList.getScores();
    }
}