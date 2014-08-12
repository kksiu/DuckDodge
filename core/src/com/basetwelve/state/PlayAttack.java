package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.handlers.StateManager;

import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by Kenneth on 8/11/14.
 */

//class where one can fling the duck
public class PlayAttack extends State {
    Image centerDuck;

    public PlayAttack(StateManager sm) {
        super(sm);

        //use texture region to create the duck actor
        TextureRegion texReg = new TextureRegion(game.getTextureHandler().getTexture("Duck"), 256, 256);

        //initialize the duck
        centerDuck = new Image(texReg);

        //resize to half
        centerDuck.setWidth(.5f * centerDuck.getWidth());
        centerDuck.setHeight(.5f * centerDuck.getHeight());
        centerDuck.setScaling(Scaling.fill);

        //set rotation origin to be in the center
        centerDuck.setOrigin((centerDuck.getWidth() / 2),
                (centerDuck.getHeight() / 2));

        //center the duck
        centerDuck.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        //fling duck
        centerDuck.addListener(new ActorGestureListener() {
            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //find the angle that the mouse was released
                float angle = (float) Math.atan2((event.getStageY() - (Gdx.graphics.getHeight() / 2)),
                        (event.getStageX() - (Gdx.graphics.getWidth() / 2)));

                angle = (float)Math.toDegrees(angle);

                float height;
                float width;

                //figure out the screen corner angles
                float sAngle = (float) Math.toDegrees(Math.atan2(Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth() / 2));
                float bAngle = (float) Math.toDegrees(Math.atan2(Gdx.graphics.getHeight() / 2, -Gdx.graphics.getWidth() / 2));

                //figure out the angles to move the duck
                if((Math.abs(angle) < sAngle) || (Math.abs(angle) > bAngle)) {
                    width = (stage.getWidth() / 2) + (centerDuck.getWidth() / 2);

                    if(Math.abs(angle) > 90) {
                        width *= -1;
                    }

                    height = (float) Math.tan(Math.toRadians(angle)) * width;

                } else {
                    height = (stage.getHeight() / 2) + (centerDuck.getHeight() / 2);

                    if(angle < 0) {
                        height *= -1;
                    }

                    width = height / (float) Math.tan(Math.toRadians(angle));
                }

                float duration = 2.0f;
                centerDuck.addAction(parallel(moveTo(width + centerDuck.getX(), height + centerDuck.getY(), duration),
                        rotateBy((new Random()).nextBoolean() ? 360f : -360f, duration)));

            }
        });

        //add it to the stage
        stage.addActor(centerDuck);

        Gdx.input.setInputProcessor(stage);


    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {

        //recenter the duck if it left the screen
        if((centerDuck.getY() + centerDuck.getHeight() <= 0) ||
                (centerDuck.getX() + centerDuck.getWidth() <= 0) ||
                (centerDuck.getY() >= Gdx.graphics.getHeight()) ||
                (centerDuck.getX() >= Gdx.graphics.getWidth())) {

            //remove all actions
            for(int i = 0; i < centerDuck.getActions().size; i++) {
                centerDuck.removeAction(centerDuck.getActions().get(i));
            }

            //realign rotation
            centerDuck.setRotation(0f);

            //center the duck
            centerDuck.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }

        //have the stage act
        stage.act(dt);
    }

    @Override
    public void render() {
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
