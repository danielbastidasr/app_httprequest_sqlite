package com.android.dani.who_wants_to_be_millionaire_.Bussiness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.android.dani.who_wants_to_be_millionaire_.Data.player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dabasra on 24/05/2015.
 */
public class mySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PlayersDB";
    // Players table name
    private static final String TABLE_PLAYERS = "players";

    // Players Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "name";
    private static final String KEY_SCORE = "score";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_SCORE};

    public mySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create player table
        String CREATE_PLAYER_TABLE = "CREATE TABLE players ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "score INTEGER )";

        // create players table
        db.execSQL(CREATE_PLAYER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older players table if existed
        db.execSQL("DROP TABLE IF EXISTS players");


        // create fresh players table
        this.onCreate(db);
    }


    public boolean existPlayer(player pl){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor =
                db.query(TABLE_PLAYERS, // a. table
                        COLUMNS, // b. column names
                        " name = ?", // c. selections
                        new String[] { pl.getName() }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 1.1. if we got results means the player already exist
        if (cursor != null) {

            return true;
        }


        return false;
    }


    public void addPlayer(player pl){
        //for logging

        if(existPlayer(pl)) return;

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();


        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, pl.getName());
        values.put(KEY_SCORE, pl.getScore());

        // 3. insert
        db.insert(TABLE_PLAYERS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }



    public player getPlayer(String name){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_PLAYERS, // a. table
                        COLUMNS, // b. column names
                        " name = ?", // c. selections
                        new String[] { String.valueOf(name) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build player object
        player pl = new player();
        pl.setId(Integer.parseInt(cursor.getString(0)));
        pl.setName(cursor.getString(1));
        pl.setScore(cursor.getInt(2));

        // 5. return player
        return pl;
    }




    public List<player> getAllPlayers() {
        List<player> players = new LinkedList<player>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_PLAYERS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=  db.query(TABLE_PLAYERS, // a. table
                COLUMNS, // b. column names
                null, // c. selections
                null, // d. selections args
                null, // e. group by
                null, // f. having
                KEY_SCORE+" DESC", // g. order by
                null);
        // 3. go over each row, build book and add it to list
        player pl = null;
        if (cursor.moveToFirst()) {
            do {
                pl = new player();
                pl.setId(Integer.parseInt(cursor.getString(0)));
                pl.setName(cursor.getString(1));
                pl.setScore(cursor.getInt(2));

                // Add player to books
                players.add(pl);

            } while (cursor.moveToNext());
        }


        // return books
        return players;
    }


    public int updatePlayer(player pl) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("name", pl.getName()); // get name
        values.put("score", pl.getScore()); // get score

        // 3. updating row
        int i = db.update(TABLE_PLAYERS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(pl.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public void deletePlayer(player pl) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_PLAYERS, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(pl.getId())}); //selections args

        // 3. close
        db.close();


    }


}
