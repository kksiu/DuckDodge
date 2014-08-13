package com.basetwelve.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.MainClass;

/**
 * Created by Kenneth on 8/12/14.
 */
public class Duck extends Image {
    private float scale;

    public Duck(MainClass game) {
        super(new TextureRegion(game.getTextureHandler().getTexture("Duck"), 256, 256));

        //use texture region to create the duck actor
        scale = (Gdx.graphics.getWidth() / 15) / getWidth();
        setWidth(0.5f * getWidth());
        setHeight(0.5f * getHeight());
        setScaling(Scaling.fill);

        setOrigin((getWidth() / 2),
                (getHeight() / 2));
    }
}
