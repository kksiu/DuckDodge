package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.basetwelve.entities.Duck;
import com.basetwelve.handlers.StateManager;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;

/**
 * Created by Kenneth on 8/11/14.
 */

//class where one can fling the duck
public class PlayAttack extends State {
    Duck centerDuck;

    public PlayAttack(StateManager sm) {
        super(sm);

        //initialize the duck
        centerDuck = new Duck(game, null);

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

                ParallelAction pAction = centerDuck.sendActorAngle(angle, centerDuck, 300f);
                pAction.addAction(repeat(RepeatAction.FOREVER, rotateBy(360, 1.0f)));
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
