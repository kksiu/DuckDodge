package com.basetwelve.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.MainClass;

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
}