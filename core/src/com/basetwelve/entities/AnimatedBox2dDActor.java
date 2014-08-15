package com.basetwelve.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Kenneth on 8/13/14.
 */
public class AnimatedBox2dDActor extends Box2DActor {
    protected Animation animation;
    private float stateTime;
    private boolean isLoop;

    //set animation
    public AnimatedBox2dDActor(Animation nAnimation, World world, boolean nLoop) {
        super(nAnimation.getKeyFrame(0), world);
        animation = nAnimation;
        isLoop = nLoop;
    }

    //on override, animate it
    @Override
    public void act(float dt) {
        ((TextureRegionDrawable)getDrawable()).setRegion(animation.getKeyFrame(stateTime+=dt, isLoop));
        super.act(dt);
    }

    public Animation getAnimation() {
        return animation;
    }

    public float getStateTime() {
        return stateTime;
    }
}
