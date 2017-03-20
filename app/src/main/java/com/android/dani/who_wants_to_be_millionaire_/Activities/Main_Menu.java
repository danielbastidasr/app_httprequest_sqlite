package com.android.dani.who_wants_to_be_millionaire_.Activities;


import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.android.dani.who_wants_to_be_millionaire_.R;

public class Main_Menu extends AppCompatActivity {

    LinearLayout play;
    LinearLayout settings;
    LinearLayout scores;
    ActionBar actionBar;
    Intent in;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        actionBar = getSupportActionBar();
        play=(LinearLayout)findViewById(R.id.buttonPlay);
        settings=(LinearLayout)findViewById(R.id.buttonSettings);
        scores=(LinearLayout)findViewById(R.id.buttonScores);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation click = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click);
                play.startAnimation(click);
                in=new Intent(getApplicationContext(),Play.class);
                startActivity(in);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_translate);
                settings.startAnimation(slide);
                in=new Intent(getApplicationContext(),Settings.class);
                startActivity(in);
            }
        });
        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation click = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_click);
                scores.startAnimation(click);
                in=new Intent(getApplicationContext(), com.android.dani.who_wants_to_be_millionaire_.Activities.scores.class);
                startActivity(in);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                in=new Intent(this,Settings.class);
                break;
            case R.id.action_play:
                in=new Intent(this,Play.class);
                break;
            case R.id.action_scores:
                in=new Intent(this,scores.class);
                break;
            case R.id.action_credits:
                in=new Intent(this,credits.class);
                break;
        }
        if (in!=null)
           startActivity(in);
        return super.onOptionsItemSelected(item);
    }
}
