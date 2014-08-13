package com.basetwelve.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Kenneth on 8/12/14.
 */

public class CircleControl extends Image {
    public float padding;
    public boolean circleActivated;
    public float circleAngle;
    public float circleHypotenuse;
    public int pointerID;

    //constructor
    public CircleControl(Texture region, float nPadding) {
        super(region);

        //initialize variables
        circleActivated = false;
        circleAngle = 0;
        circleHypotenuse = 0;

        //set padding
        padding = nPadding;

        pointerID = -1;

        addListener(new ClickListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                circleActivated = true;
                circleAngle = getAngle(event);
                circleHypotenuse = getHypotenuse(event);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                circleAngle = getAngle(event);
                circleHypotenuse = getHypotenuse(event);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                circleActivated = false;
                circleAngle = 0;
                circleHypotenuse = 0;
            }
        });
    }

    //hit detection is now a circle
    @Override
    public Actor hit (float x, float y, boolean touchable) {
        Circle circle = new Circle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), (padding * 2) + (getWidth() / 2));

        if(circle.contains(x, y)) {
            return this;
        }

        return null;
    }

    private float getAngle(InputEvent event) {
        return (float) Math.atan2(event.getStageY() - (getY() + (getHeight() / 2)),
                event.getStageX() - (getX()  + (getWidth() / 2)));
    }

    private float getHypotenuse(InputEvent event) {
        return (float) Math.sqrt(Math.pow(event.getStageY() - (getY() + (getHeight() / 2)), 2) +
                Math.pow(event.getStageX() - (getX()  + (getWidth() / 2)), 2));
    }

}
