package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.entities.CircleControl;
import com.basetwelve.entities.Dodger;
import com.basetwelve.entities.Duck;
import com.basetwelve.handlers.StateManager;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Created by Kenneth on 8/11/14.
 */
public class PlayDodge extends State {
    //player dodge variables
    private Dodger playerDodge;

    //control variables
    private CircleControl controlMove;

    //movement keys
    private boolean moveLeftKey, moveRightKey, moveUpKey, moveDownKey;

    //movement based off of control circles
    private boolean moveCircle;
    private float moveAngle;
    private float moveHypotenuse;

    private float circlePadding = Gdx.graphics.getWidth() / 60;

    //movement speed
    private float movementSpeed = Gdx.graphics.getWidth() / 5;

    //list of ducks
    private List<Duck> duckList;

    //random object for duck entrances
    private Random rand;
    private Timer duckTimer;
    private int duckCount;
    private float duration = 0.5f;
    private Timer.Task duckTask;

    public PlayDodge(StateManager sm) {
        super(sm);

        //initialize random
        rand = new Random();

        //list of ducks
        duckList = new ArrayList<Duck>();

        //movement keys
        moveLeftKey = false;
        moveRightKey = false;
        moveUpKey = false;
        moveDownKey = false;

        //movement circle
        moveCircle = false;
        moveAngle = 0;

        //make a texture region based off of a person
        TextureRegion texReg = new TextureRegion(game.getTextureHandler().getTexture("Player"), 17, 21);

        //now make a dodger
        playerDodge = new Dodger(texReg);

        float dodgerScale = (Gdx.graphics.getWidth() / 20) / playerDodge.getWidth();
        playerDodge.setWidth(dodgerScale * playerDodge.getWidth());
        playerDodge.setHeight(dodgerScale * playerDodge.getHeight());
        playerDodge.setScaling(Scaling.fill);

        //set in center
        playerDodge.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);


        //add circle control
        controlMove = new CircleControl(game.getTextureHandler().getTexture("CircleControl"), circlePadding);
        controlMove.setPosition(circlePadding, circlePadding);

        //scale it down
        float controlScale = (Gdx.graphics.getWidth() / 6f) / controlMove.getWidth();
        controlMove.setWidth(controlScale * controlMove.getWidth());
        controlMove.setHeight(controlScale * controlMove.getHeight());
        controlMove.setScaling(Scaling.fill);

        controlMove.addListener(new ClickListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                moveCircle = true;
                moveAngle = getAngle(event, controlMove);
                moveHypotenuse = getHypotenuse(event, controlMove);
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                moveAngle = getAngle(event, controlMove);
                moveHypotenuse = getHypotenuse(event, controlMove);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                moveCircle = false;
                moveAngle = 0;
                moveHypotenuse = 0;
            }
        });

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
        stage.addActor(controlMove);

        duckTask = new Timer.Task() {
            @Override
            public void run() {
                sendPrivateduck();
            }
        };

        //add random ducks
        duckTimer = new Timer();
        duckTimer.scheduleTask(duckTask, 0.0f, duration);
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
        //move actor based off of the keys pressed
        if(moveCircle) {
            if( (moveHypotenuse >= (controlMove.getWidth() / 6)) &&
                    (moveHypotenuse <= ((controlMove.getWidth() / 2) + controlMove.padding)) ) {
                //use angle to determine movement
                float move = dt * movementSpeed;

                //time to move by this much based off of the angle
                //playerDodge.addAction(parallel(moveBy((float) Math.cos(moveAngle) * move, (float) Math.sin(moveAngle) * move)));
                playerDodge.moveBy((float) Math.cos(moveAngle) * move, (float) Math.sin(moveAngle) * move);
            }
        } else {
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
        }

        //get all the duck lists
        for(int i = 0; i < duckList.size(); i++) {
            Duck duck = duckList.get(i);
            //if all actions are done (i.e., off the screen), remove
            if(duck.getActions().size == 1) {
                duckList.remove(duck);
                duck.remove();
            }
        }

        //check the duck and see if it is time to make it faster
        if(duckCount == 5) {
            if(duration >= 1.0f) {
                duration = duration - 0.5f;
                duckCount = 0;
                duckTask.cancel();
                duckTimer.stop();
                duckTimer.scheduleTask(duckTask, 0.0f, duration);
                duckTimer.start();
            }
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

    private float getAngle(InputEvent event, CircleControl control) {
        return (float) Math.atan2(event.getStageY() - (control.getY() + (control.getHeight() / 2)),
                event.getStageX() - (control.getX()  + (control.getWidth() / 2)));
    }

    private float getHypotenuse(InputEvent event, CircleControl control) {
        return (float) Math.sqrt(Math.pow(event.getStageY() - (control.getY() + (control.getHeight() / 2)), 2) +
        Math.pow(event.getStageX() - (control.getX()  + (control.getWidth() / 2)), 2));
    }

    private void sendPrivateduck() {
        float startX = 0f;
        float startY = 0f;
        float endX = 0f;
        float endY = 0f;

        int sign = rand.nextBoolean() ? 1 : -1;

        //check if start from width or height
        boolean startWidth = rand.nextBoolean();
        if(startWidth) {
            startX = Gdx.graphics.getWidth();
            startY = rand.nextFloat() * Gdx.graphics.getHeight();
        } else {
            startX = rand.nextFloat() * Gdx.graphics.getWidth();
            startY = Gdx.graphics.getHeight();
        }

        //now determine the end points (make sure that it is not the opposite
        int endSide = rand.nextInt(5);
        if(startWidth) {
            switch(endSide) {
                //top side
                case 0:
                    endX = rand.nextFloat() * Gdx.graphics.getWidth();
                    endY = Gdx.graphics.getHeight();
                    break;

                //bottom side
                case 1:
                    endX = rand.nextFloat() * Gdx.graphics.getWidth();
                    endY = 0;
                    break;

                //opposite
                default:
                    endX = -1 * startX;
                    endY = rand.nextFloat() * Gdx.graphics.getHeight();
                    break;
            }

        } else {
            switch(endSide) {
                //left side
                case 0:
                    endX = 0;
                    endY = rand.nextFloat() * Gdx.graphics.getHeight();
                    break;

                //right side
                case 1:
                    endX = Gdx.graphics.getWidth();
                    endY = rand.nextFloat() * Gdx.graphics.getHeight();
                    break;

                //do the opposite side
                default:
                    endX = rand.nextFloat() * Gdx.graphics.getWidth();
                    endY = -1 * startY;
                    break;
            }

        }

        Duck duck = Duck.sendDuck(startX, startY, endX, endY, 100f, game);
        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(parallel(rotateBy(360, 1.0f)));
        repeatAction.setCount(RepeatAction.FOREVER);
        duck.addAction(repeatAction);

        duckList.add(duck);
        stage.addActor(duck);
        duck.toBack();

        duckCount++;
    }
}
