package com.basetwelve.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Kenneth on 8/12/14.
 */

public class CircleControl extends Image {
    public float padding;

    //constructor
    public CircleControl(Texture region, float nPadding) {
        super(region);

        //set padding
        padding = nPadding;
    }

    //hit detection is now a circle
    @Override
    public Actor hit (float x, float y, boolean touchable) {
        Circle circle = new Circle(getX() + (getWidth() / 2), getY() + (getHeight() / 2), padding + (getWidth() / 2));

        if(circle.contains(x, y)) {
            return this;
        }

        return null;
    }

}
