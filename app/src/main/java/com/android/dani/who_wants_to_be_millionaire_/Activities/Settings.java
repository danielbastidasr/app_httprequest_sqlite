package com.android.dani.who_wants_to_be_millionaire_.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.dani.who_wants_to_be_millionaire_.Data.HighScoreList;
import com.android.dani.who_wants_to_be_millionaire_.R;
import com.android.dani.who_wants_to_be_millionaire_.Bussiness.SaveFriendWeb;
import com.android.dani.who_wants_to_be_millionaire_.Bussiness.mySQLiteHelper;
import com.android.dani.who_wants_to_be_millionaire_.Data.player;

public class Settings extends AppCompatActivity {
    Button addfriend;
    Spinner options;
    ActionBar ab;
    EditText name;
    SharedPreferences.Editor editor;
    SharedPreferences prefs;
    HighScoreList scoreFriendList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name=(EditText)findViewById(R.id.name);
        addfriend = (Button) findViewById(R.id.buttonaddfriend);
        options = (Spinner) findViewById(R.id.opciones);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        // We are gonna use the shared preference_file to save preferences from Settings
        prefs = getSharedPreferences(getString(R.string.preference_file), MODE_PRIVATE);
        editor = prefs.edit();
        name.setText(prefs.getString("userName", "Unnamed"));
        name.setHint(prefs.getString("userName", "Unnamed"));//Unnamed default user name
        options.setSelection(4);// 3 help option
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString() != prefs.getString("userName","Unnamed") ) {
                    editor.putString("userName", name.getText().toString());
                    editor.putBoolean("userNameChanged", true);
                    editor.commit();
                    //If player already exist don't add a new one
                    mySQLiteHelper db = new mySQLiteHelper(getApplicationContext());
                    SaveFriendWeb saveFW= new SaveFriendWeb();
                    String myTaskParams[] = new String[2];
                    myTaskParams[0] = new String("Unnamed");
                    myTaskParams[1] = new String(name.getText().toString());
                    saveFW.execute(myTaskParams);
                    // this case if there was a opened game and you changed the user name
                    if (prefs.getInt("price",0)!=0) {
                        db.addPlayer(new player(prefs.getString("userName", "Unnamed"), prefs.getInt("price", 0)));
                    }
                }
                switch (options.getSelectedItemPosition()) {
                    case 1:
                        editor.putInt("helpNumber", 0);
                        editor.commit();
                        break;
                    case 2:
                        editor.putInt("helpNumber", 1);
                        editor.commit();
                        break;
                    case 3:
                        editor.putInt("helpNumber", 2);
                        editor.commit();
                        break;
                    default:
                        editor.putInt("helpNumber", 3);
                        editor.commit();
                        break;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                builder.setMessage("You got a new friend: " + prefs.getString("userName", "Unnamed"))
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }





}
