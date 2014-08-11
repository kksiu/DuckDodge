package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.Handlers.StateManager;

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

        //center the duck
        centerDuck.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        //fling duck
        centerDuck.addListener(new ActorGestureListener() {
            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                System.out.println("MADE IT");
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
