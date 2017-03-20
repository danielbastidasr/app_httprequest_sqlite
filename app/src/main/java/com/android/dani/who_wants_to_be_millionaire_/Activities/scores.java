package com.android.dani.who_wants_to_be_millionaire_.Activities;

import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.dani.who_wants_to_be_millionaire_.Bussiness.HighScore;
import com.android.dani.who_wants_to_be_millionaire_.Data.HighScoreList;
import com.android.dani.who_wants_to_be_millionaire_.R;
import com.android.dani.who_wants_to_be_millionaire_.Bussiness.mySQLiteHelper;
import com.android.dani.who_wants_to_be_millionaire_.Data.player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class scores extends AppCompatActivity {
    TabHost host;
    TabHost.TabSpec spec;
    ActionBar ab;
    mySQLiteHelper db;
    List<player> l;
    TableLayout tableFriends ;
    ProgressDialog pDialog;
    boolean tableFull;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        tableFull=false;
        host = (TabHost) findViewById(android.R.id.tabhost);
        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState); // state will be bundle your activity state which you get in onCreate
        host.setup(mLocalActivityManager);

        TableLayout table = (TableLayout) findViewById(R.id.tabtable);
        tableFriends = (TableLayout) findViewById(R.id.tabtableFriends);

        db =new mySQLiteHelper(this);

        l=db.getAllPlayers();
        int N=l.size();
        for (int i=0;i<N; i++) {
            // if the player has not played yet, don't show him
            if (l.get(i).getScore()==0) break;
            TableRow row = new TableRow(this);

            TextView tv = new TextView(this);
            tv.setText(l.get(i).getName());
            row.addView(tv);

            tv = new TextView(this);
            tv.setText("" + l.get(i).getScore());
            row.addView(tv);
            table.addView(row);
        }

        TabHost.TabSpec tab1 = host.newTabSpec("Tab1");
        tab1.setIndicator("Local");
        tab1.setContent(R.id.tabtable);

        TabHost.TabSpec tab2 = host.newTabSpec("Tab2");
        tab2.setIndicator("Friends");
        tab2.setContent(R.id.tabtableFriends);

        host.addTab(tab1);
        host.addTab(tab2);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId=="Tab2" && !tableFull) {
                    GetContacts s = (GetContacts) new GetContacts().execute();
                    tableFull=true;
                }
                invalidateOptionsMenu();
            }
        });
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == 10) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.deleteScores))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            int N=l.size();
                            for(int i=0; i<N; i++){
                                db.deletePlayer(l.get(i));
                            }
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (host.getCurrentTabTag()=="Tab1") {
            MenuItem mi =menu.add(Menu.NONE, 10, menu.NONE, "Delete");
            mi.setIcon(R.drawable.trash);
            mi.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        else menu.removeItem(10);
        return super.onPrepareOptionsMenu(menu);
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        private String urlScoreFriends="http://wwtbamandroid.appspot.com/rest/highscores?name=Unnamed";
        HighScoreList scoreFriendList;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(scores.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
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
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            List<HighScore> lhs= scoreFriendList.getScores();
            for (int i=0;i<lhs.size(); i++) {
                TableRow row = new TableRow(scores.this);
                TextView tv = new TextView(scores.this);
                tv.setText(lhs.get(i).getName());
                row.addView(tv);

                tv = new TextView(scores.this);
                tv.setText("" + lhs.get(i).getScoring());
                row.addView(tv);
                tableFriends.addView(row);
            }
        }
    }
}






