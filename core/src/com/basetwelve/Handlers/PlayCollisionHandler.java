package com.basetwelve.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.basetwelve.entities.Dodger;

/**
 * Created by Kenneth on 8/13/14.
 */
public class PlayCollisionHandler implements ContactListener{

    public PlayCollisionHandler() {
        super();
    }

    @Override
    public void beginContact(Contact contact) {

        if(contact.getFixtureA().getUserData().getClass().equals(Dodger.class)
                || contact.getFixtureB().getUserData().getClass().equals(Dodger.class)) {
            System.out.println("CONTACT");
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

}
