package com.basetwelve.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Kenneth on 8/14/14.
 */
//will create a bullet
public class Bullet extends Box2DActor {
    public Bullet(TextureRegion texRegion, World nWorld) {
        super(texRegion, nWorld);
    }
}
