package com.basetwelve.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.basetwelve.entities.Box2DActor;
import com.basetwelve.entities.Bullet;
import com.basetwelve.entities.Dodger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenneth on 8/13/14.
 */
public class PlayCollisionHandler implements ContactListener{
    List<Box2DActor> actorsToRemoveFromDodger;
    List<Box2DActor> actorsToRemoveFromBullet;
    List<Vector2> pointsToExplodeFromDodger;
    List<Vector2> pointsToExplodeFromBullet;

    public PlayCollisionHandler() {
        super();
        actorsToRemoveFromDodger = new ArrayList<Box2DActor>();
        actorsToRemoveFromBullet = new ArrayList<Box2DActor>();
        pointsToExplodeFromDodger = new ArrayList<Vector2>();
        pointsToExplodeFromBullet = new ArrayList<Vector2>();
    }

    @Override
    public void beginContact(Contact contact) {

        Box2DActor actor1 = (Box2DActor) contact.getFixtureA().getUserData();
        Box2DActor actor2 = (Box2DActor) contact.getFixtureB().getUserData();

        //make sure that the bodies is not the player
        if( actor1.getClass().equals(Dodger.class)
                || actor2.getClass().equals(Dodger.class) ) {

            Box2DActor actor;

            //remove the non doger actor
            if(actor1.getClass().equals(Dodger.class)) {
                actor = actor2;
                actorsToRemoveFromDodger.add(actor2);
            } else {
                actor = actor1;
                actorsToRemoveFromDodger.add(actor1);
            }

            //set them to be removed
            pointsToExplodeFromDodger.add(new Vector2(actor.getCenterX(), actor.getCenterY()));
        }

        //now check if it was a bullet collision
        if( actor1.getClass().equals(Bullet.class)
                || actor2.getClass().equals(Bullet.class) ) {
            actorsToRemoveFromBullet.add(actor1);
            actorsToRemoveFromBullet.add(actor2);

            //set the explosion
            pointsToExplodeFromBullet.add(contact.getWorldManifold().getPoints()[0]);
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    public List<Box2DActor> getActorsToRemoveFromDodger() {
        return actorsToRemoveFromDodger;
    }

    public List<Box2DActor> getActorsToRemoveFromBullet() {
        return actorsToRemoveFromBullet;
    }

    public List<Vector2> getPointsToExplodeFromDodger() {
        return pointsToExplodeFromDodger;
    }

    public List<Vector2> getPointsToExplodeFromBullet() {
        return pointsToExplodeFromBullet;
    }
}
