package com.android.dani.who_wants_to_be_millionaire_.Data;

/**
 * Created by dabasra on 24/05/2015.
 */
public class player {

    private int id;
    private String name;
    private int score;

    public player(String name, int score) {
        super();
        this.name = name;
        this.score = score;
    }

    public  player(){};

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
