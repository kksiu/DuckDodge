package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.entities.Dodger;
import com.basetwelve.handlers.StateManager;

/**
 * Created by Kenneth on 8/11/14.
 */
public class PlayDodge extends State {
    private Dodger playerDodge;

    //movement keys
    boolean moveLeftKey, moveRightKey, moveUpKey, moveDownKey;

    //movement speed
    private float movementSpeed = 100.0f;

    public PlayDodge(StateManager sm) {
        super(sm);

        //movement keys
        moveLeftKey = false;
        moveRightKey = false;
        moveUpKey = false;
        moveDownKey = false;

        //make a texture region based off of a person
        TextureRegion texReg = new TextureRegion(game.getTextureHandler().getTexture("Player"), 17, 21);

        //now make a dodger
        playerDodge = new Dodger(texReg);

        float scale = 3.0f;

        playerDodge.setWidth(scale * playerDodge.getWidth());
        playerDodge.setHeight(scale * playerDodge.getHeight());
        playerDodge.setScaling(Scaling.fill);

        //set in center
        playerDodge.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        //add a stage listener for when the player moves
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                handleKeys(keycode, true);
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                handleKeys(keycode, false);
                return true;
            }
        });

        Gdx.input.setInputProcessor(stage);

        //make the dodger show up on the screen
        stage.addActor(playerDodge);
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
        if(moveUpKey) {
            playerDodge.addAction(parallel(moveBy(0, dt * movementSpeed)));
        }

        if(moveLeftKey) {
            playerDodge.addAction(parallel(moveBy(-dt * movementSpeed, 0)));
        }

        if(moveDownKey) {
            playerDodge.addAction(parallel(moveBy(0, -dt * movementSpeed)));
        }

        if(moveRightKey) {
            playerDodge.addAction(parallel(moveBy(dt * movementSpeed, 0)));
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
        stage.dispose();
    }

    //handles the key
    public void handleKeys(int keycode, boolean pressDown) {
        if(keycode == Input.Keys.W) {
            moveUpKey = pressDown;
        } else if(keycode == Input.Keys.A) {
            moveLeftKey = pressDown;
        } else if(keycode == Input.Keys.S) {
            moveDownKey = pressDown;
        } else if(keycode == Input.Keys.D) {
            moveRightKey = pressDown;
        }
    }
}
