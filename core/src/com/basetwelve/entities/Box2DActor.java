package com.basetwelve.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Kenneth on 8/13/14.
 */
public class Box2DActor extends Image {
    //box2D container
    Body body;
    World world;

    //constructor
    protected Box2DActor(TextureRegion region, World nWorld) {
        super(region);

        if(nWorld != null) {
            world = nWorld;
        }
    }

    //set the body
    public void setBody() {
        //define body and fixture
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = 1;
        fixtureDef.filter.maskBits = 1;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        //move the body with it
        body.setTransform(getX() + (getWidth() / 2), getY() + (getHeight() / 2), 0);

    }

    @Override
    public boolean remove() {
        boolean rBool = super.remove();
        return rBool;
    }

    public Body getBody() {
        return body;
    }

}
