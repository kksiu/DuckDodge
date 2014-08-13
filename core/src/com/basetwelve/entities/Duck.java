package com.basetwelve.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.MainClass;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;

/**
 * Created by Kenneth on 8/12/14.
 */
public class Duck extends Box2DActor {
    private float scale;

    public Duck(MainClass game, World nWorld) {
        super(new TextureRegion(game.getTextureHandler().getTexture("Duck"), 256, 256), nWorld);

        //use texture region to create the duck actor
        scale = (Gdx.graphics.getWidth() / 15) / getWidth();
        setWidth(scale * getWidth());
        setHeight(scale * getHeight());
        setScaling(Scaling.fill);

        setOrigin((getWidth() / 2),
                (getHeight() / 2));
    }

    //sends a duck at a position from one point to another point
    public static Duck sendDuck(float startX, float startY, float endX, float endY, float speed, MainClass game, World world) {
        Duck duck = new Duck(game, world);
        duck.setBody();

        //start the duck at the original location
        if(startX <= 0) {
            duck.setX(-duck.getWidth());
            duck.setY(startY);
        } else if(startX >= Gdx.graphics.getWidth()) {
            duck.setX(Gdx.graphics.getWidth());
            duck.setY(startY);
        } else if(startY <= 0) {
            duck.setX(startX);
            duck.setY(-duck.getHeight());
        } else if(startY >= Gdx.graphics.getHeight()) {
            duck.setX(startX);
            duck.setY(Gdx.graphics.getHeight());
        } else {
            duck.setX(startX);
            duck.setY(startY);
        }

        //end location
        if(endX <= 0) {
            endX = -duck.getWidth();
        } else if(endX >= Gdx.graphics.getWidth()) {
            endX = Gdx.graphics.getWidth();
        } else if(endY <= 0) {
            endY = -duck.getHeight();
        } else if(endY >= Gdx.graphics.getHeight()) {
            endY = Gdx.graphics.getHeight();
        }

        //get the distance of the movement
        float distance = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

        //now add a move to function with the speed
        duck.addAction(parallel(moveTo(endX, endY, distance / speed)));

        return duck;
    }
}
