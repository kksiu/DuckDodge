package com.basetwelve.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Kenneth on 8/11/14.
 */
public class Dodger extends Box2DActor {

    //need a score
    private int score;

    //need health
    private int health;

    //constructor
    public Dodger(TextureRegion region, World nWorld) {
        super(region, nWorld);

        //start at 5 health and score of 0
        score = 0;
        health = 5;
    }

    //getters and setters
    public int getScore() {
        return score;
    }

    public int getHealth() {
        return health;
    }

    public void setScore(int nScore) {
        score = nScore;
    }

    public void setHealth(int nHealth) {
        health = nHealth;
    }

}
