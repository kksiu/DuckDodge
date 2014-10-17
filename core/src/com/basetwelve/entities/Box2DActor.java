package com.basetwelve.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;

/**
 * Created by Kenneth on 8/13/14.
 */
public class Box2DActor extends Image {
    //box2D container
    private Body body;
    private World world;
    double center;

    //constructor
    protected Box2DActor(TextureRegion region, World nWorld) {
        super(region);

        //set the center of the image


        if(nWorld != null) {
            world = nWorld;
        }
    }

    public void setCenterPosition(float x, float y) {
        this.setPosition(x, y, Align.center);
    }

    public float getCenterX() {
        return this.getX(Align.center);
    }

    public float getCenterY() {
        return this.getY(Align.center);
    }

    //set the body
    public void setBody(short categoryBits, short maskBits) {
        //define body and fixture
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth() / 2, getHeight() / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = categoryBits;
        fixtureDef.filter.maskBits = maskBits;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        //move the body with it
        if(body != null) {
            body.setTransform(getX() + (getWidth() / 2), getY() + (getHeight() / 2), (float) Math.toRadians(getRotation()));
        }
    }

    @Override
    public boolean remove() {
        return super.remove();
    }

    public Body getBody() {
        return body;
    }

    //sends a duck at a position from one point to another point
    public void sendActor(float startX, float startY, float endX, float endY, float speed, short categoryBits, short maskBits) {
        setBody(categoryBits, maskBits);

        //start the duck at the original location
        if(startX <= 0) {
            setX(-getWidth());
            setY(startY);
        } else if(startX >= Gdx.graphics.getWidth()) {
            setX(Gdx.graphics.getWidth());
            setY(startY);
        } else if(startY <= 0) {
            setX(startX);
            setY(-getHeight());
        } else if(startY >= Gdx.graphics.getHeight()) {
            setX(startX);
            setY(Gdx.graphics.getHeight());
        } else {
            setX(startX);
            setY(startY);
        }

        //end location
        if(endX <= 0) {
            endX = -getWidth();
        } else if(endX >= Gdx.graphics.getWidth()) {
            endX = Gdx.graphics.getWidth();
        } else if(endY <= 0) {
            endY = -getHeight();
        } else if(endY >= Gdx.graphics.getHeight()) {
            endY = Gdx.graphics.getHeight();
        }

        //get the distance of the movement
        float distance = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

        //now add a move to function with the speed
        addAction(parallel(moveTo(endX, endY, distance / speed)));
    }

    //send actor based off an angle
    public ParallelAction sendActorAngle(float angle, Box2DActor player, float speed) {

        float height;
        float width;

        float padding = getWidth();

        //set position
        setCenterPosition(player.getCenterX(), player.getCenterY());

        //figure out the angles to move the actor
        if(Gdx.graphics.getWidth() > Gdx.graphics.getHeight()) {
            if(Math.abs(angle) > 90) {
                width = -(player.getX() + player.getWidth());
                width -= padding;
            } else {
                width = Gdx.graphics.getWidth() - player.getX();
                width += padding;
            }

            height = (float) Math.tan(Math.toRadians(angle)) * width;
        } else {
            if(angle > 0) {
                height = Gdx.graphics.getHeight() - player.getY();
                height += padding;
            } else {
                height = -(player.getY() + player.getHeight());
                height -= padding;
            }

            width = height / (float) Math.tan(Math.toRadians(angle));
        }

        //get the distance of the movement
        float distance = (float) Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));

        ParallelAction returnParallelAction = parallel(moveTo(width + player.getX(), height + player.getY(), distance / speed));
        addAction(returnParallelAction);

        return returnParallelAction;
    }

    public boolean isOutOfBounds() {
        if(this.getStage() == null) {
            return false;
        }

        //checks if the actor is out of bounds
        if( (this.getX() + this.getWidth() < 0) ||
                (this.getX() > this.getStage().getWidth()) ||
                (this.getY() + this.getHeight() < 0 ) ||
                (this.getY() > this.getStage().getHeight()) ) {
            return true;
        }

        return false;
    }

    public void removeBody() {
        Array<Body> listOfBodies = new Array<Body>();
        world.getBodies(listOfBodies);
        if((body != null) && listOfBodies.contains(body, true)) {
            body.setActive(false);
            world.destroyBody(body);
        }

        listOfBodies.clear();
    }

}
