package com.android.dani.who_wants_to_be_millionaire_.Activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.dani.who_wants_to_be_millionaire_.Data.HighScoreList;
import com.android.dani.who_wants_to_be_millionaire_.Data.Question;
import com.android.dani.who_wants_to_be_millionaire_.R;
import com.android.dani.who_wants_to_be_millionaire_.Bussiness.SaveScore;
import com.android.dani.who_wants_to_be_millionaire_.Bussiness.mySQLiteHelper;
import com.android.dani.who_wants_to_be_millionaire_.Bussiness.parserXmlToClass;
import com.android.dani.who_wants_to_be_millionaire_.Data.player;

import java.io.InputStream;

public class Play extends AppCompatActivity {

    TextView question;
    TextView questionNumber;
    TextView qPrice;
    Button [] listOptions=new Button[4] ;
    Button call,people,fifty,exit;
    int qNumber;
    int price;
    int help;
    Question actualq;
    parserXmlToClass parse;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    mySQLiteHelper db;

    Boolean bFifty,bPeople,bCall;

    HighScoreList scoreFriendList;
    InputStream xml=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        qPrice=(TextView)findViewById(R.id.questionPrice);
        question=(TextView)findViewById(R.id.question);
        questionNumber=(TextView)findViewById(R.id.questionNumber);
        listOptions[0]=(Button)findViewById(R.id.opcion1);
        listOptions[1]=(Button)findViewById(R.id.opcion2);
        listOptions[2]=(Button)findViewById(R.id.opcion3);
        listOptions[3]=(Button)findViewById(R.id.opcion4);

        call=(Button)findViewById(R.id.buttonCall);
        people=(Button)findViewById(R.id.buttonPeople);
        fifty=(Button)findViewById(R.id.buttonFifty);
        exit=(Button)findViewById(R.id.buttonExit);

        //Prepare all variables we are gonna need
        prefs = getSharedPreferences(getString(R.string.preference_file), MODE_PRIVATE);
        editor= prefs.edit();
        db =new mySQLiteHelper(this);
        if(prefs.getBoolean("playChangeOrientation",false) &&
                !prefs.getBoolean("userNameChanged",false )){
            // if it was closed before AND if userName didn't change
            editor.putBoolean("playChangeOrientation", false);
            editor.putBoolean("userNameChanged",false);
            editor.commit();

            qNumber = prefs.getInt("qNumber", 0);
            price = prefs.getInt("price", 0);
            help = prefs.getInt("help", 3);
            bFifty = prefs.getBoolean("bFifty", false);
            bPeople = prefs.getBoolean("bPeople", false);
            bCall = prefs.getBoolean("bCall", false);
        }
        else {
            qNumber = 0;
            price =  0;
            help = prefs.getInt("helpNumber",3);
            bFifty = false;
            bPeople = false;
            bCall = false;
        }
        // we assume that the xml with the questions its saved in resurse/raw/questions.xml
        xml=getResources().openRawResource(R.raw.questions);
        // Initialize all variables to the 0 state which is the question 0 and the initial settings
        qPrice.setText(""+(price+100));
        parse=new parserXmlToClass(xml);
        actualq=parse.getQuestion(qNumber);
        //actualq contain the question
        questionNumber.setText("" + actualq.getNumber());
        question.setText(actualq.getText());
        listOptions[0].setText(actualq.getAnswer1());
        listOptions[1].setText(actualq.getAnswer2());
        listOptions[2].setText(actualq.getAnswer3());
        listOptions[3].setText(actualq.getAnswer4());

        fifty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (help == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noMoreHelp), Toast.LENGTH_LONG).show();
                } else {
                    help--;
                    bFifty=true;
                    onclick();
                }
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (help == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noMoreHelp), Toast.LENGTH_LONG).show();
                } else {
                    help--;
                    bCall = true;
                    listOptions[actualq.getPhone()-1].setBackgroundColor(Color.BLUE);
                }
            }
        });
        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (help == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noMoreHelp), Toast.LENGTH_LONG).show();
                } else {
                    help--;
                    bPeople = true;
                    listOptions[actualq.getAudience() - 1].setBackgroundColor(Color.BLUE);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.addPlayer(new player(prefs.getString("userName","Unnamed"), price));
                String myTaskParams []=new String [2];
                myTaskParams[0]=new String(prefs.getString("userName","Unnamed"));
                myTaskParams[1]=new String(""+price);
                SaveScore ss= (SaveScore) new SaveScore ().execute(myTaskParams);

                qNumber=0;
                price=0;
                help=prefs.getInt("helpNumber",3);
                bFifty=false;
                bPeople=false;
                bCall=false;
                finish();
            }
        });
        listOptions[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (actualq.getRight() == 1) { //if this is the right answer
                    acierto();
                }
                else {
                    Toast.makeText(getApplicationContext(),getString(R.string.wrongAnswer), Toast.LENGTH_LONG).show();
                    db.addPlayer(new player(prefs.getString("userName", "Unnamed"), price));

                    String myTaskParams []=new String [2];
                    myTaskParams[0]=new String(prefs.getString("userName","Unnamed"));
                    myTaskParams[1]=new String(""+price);
                    SaveScore ss= (SaveScore) new SaveScore ().execute(myTaskParams);
                    finish();
                }
            }
        });
        listOptions[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (actualq.getRight() == 2) { //if this is the right answer
                    acierto();
                } else {
                    Toast.makeText(getApplicationContext(),getString(R.string.wrongAnswer), Toast.LENGTH_LONG).show();
                    db.addPlayer(new player(prefs.getString("userName", "Unnamed"), price));
                    finish();
                }
            }
        });
        listOptions[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (actualq.getRight() == 3) { //if this is the right answer
                    acierto();
                }
                else {
                    Toast.makeText(getApplicationContext(),getString(R.string.wrongAnswer), Toast.LENGTH_LONG).show();
                    db.addPlayer(new player(prefs.getString("userName", "Unnamed"), price));
                    finish();
                }
            }
        });
        listOptions[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (actualq.getRight() == 4) { //if this is the right answer
                    acierto();
                }
                else {
                    Toast.makeText(getApplicationContext(),getString(R.string.wrongAnswer), Toast.LENGTH_LONG).show();
                    db.addPlayer(new player(prefs.getString("userName", "Unnamed"), price));
                    finish();
                }
            }
        });

        if(bFifty)  {
            help++;
            fifty.performClick();
        }
        if(bPeople) {
            help++;
            people.performClick();
        }
        if(bCall)   {
            help++;
            call.performClick();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    public void onclick(){
        listOptions[actualq.getFifty1()-1].setVisibility(Button.INVISIBLE);
        listOptions[actualq.getFifty2()- 1].setVisibility(Button.INVISIBLE);
    }


    public void acierto(){
        for(int i=0; i<4 ; i++) {
            listOptions[i].setVisibility(Button.VISIBLE);
            listOptions[i].setBackgroundColor(Color.RED);
        }
        price+=100;
        qNumber++; // question number = question number + 1
        actualq=parse.getQuestion(qNumber);
        //actualq contain the next question
        qPrice.setText("" + (price + 100));
        questionNumber.setText("" + actualq.getNumber());
        question.setText(actualq.getText());
        listOptions[0].setText(actualq.getAnswer1());
        listOptions[1].setText(actualq.getAnswer2());
        listOptions[2].setText(actualq.getAnswer3());
        listOptions[3].setText(actualq.getAnswer4());
        //this.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putBoolean("playChangeOrientation", true);
        editor.putInt("qNumber", qNumber);
        editor.putInt("price", price);
        editor.putInt("help", help);
        editor.putBoolean("bFifty", bFifty);
        editor.putBoolean("bPeople", bPeople);
        editor.putBoolean("bCall", bCall);
        editor.putBoolean("userNameChanged",false);
        editor.commit();
    }

}
