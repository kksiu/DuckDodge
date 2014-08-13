package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.entities.CircleControl;
import com.basetwelve.entities.Dodger;
import com.basetwelve.entities.Duck;
import com.basetwelve.handlers.PlayCollisionHandler;
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

    //circle for shooting
    private CircleControl controlShoot;

    private float circlePadding = Gdx.graphics.getWidth() / 30;

    //movement keys
    private boolean moveLeftKey, moveRightKey, moveUpKey, moveDownKey;

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

    //Box2dWorld
    World world;
    Box2DDebugRenderer b2dr;

    public PlayDodge(StateManager sm) {
        super(sm);

        //create box2D world (don't need physics)
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new PlayCollisionHandler());
        b2dr = new Box2DDebugRenderer();

        //initialize random
        rand = new Random();

        //list of ducks
        duckList = new ArrayList<Duck>();

        //movement keys
        moveLeftKey = false;
        moveRightKey = false;
        moveUpKey = false;
        moveDownKey = false;

        //make a texture region based off of a person
        TextureRegion texReg = new TextureRegion(game.getTextureHandler().getTexture("Player"), 17, 21);

        //now make a dodger
        playerDodge = new Dodger(texReg, world);

        float dodgerScale = (Gdx.graphics.getWidth() / 20) / playerDodge.getWidth();
        playerDodge.setWidth(dodgerScale * playerDodge.getWidth());
        playerDodge.setHeight(dodgerScale * playerDodge.getHeight());
        playerDodge.setScaling(Scaling.fill);
        playerDodge.setBody();

        //set in center
        playerDodge.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        //add circle control
        controlMove = new CircleControl(game.getTextureHandler().getTexture("CircleControl"), circlePadding);

        //scale it down
        float controlScale = (Gdx.graphics.getWidth() / 6f) / controlMove.getWidth();
        controlMove.setWidth(controlScale * controlMove.getWidth());
        controlMove.setHeight(controlScale * controlMove.getHeight());
        controlMove.setScaling(Scaling.fill);

        //add shooting
        controlShoot = new CircleControl(game.getTextureHandler().getTexture("CircleControl"), circlePadding);
        controlShoot.setWidth(controlScale * controlShoot.getWidth());
        controlShoot.setHeight(controlScale * controlShoot.getHeight());
        controlShoot.setScaling(Scaling.fill);

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

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!controlMove.circleActivated && (event.getStageX() <= (Gdx.graphics.getWidth() / 2) - (controlMove.getWidth() / 2))) {
                    //handle the touch down event
                    controlMove.setCenterPosition(event.getStageX(), event.getStageY());
                    controlMove.getListeners().get(0).handle(event);
                    controlMove.pointerID = pointer;

                    //add to the stage
                    stage.addActor(controlMove);
                }

                if(!controlShoot.circleActivated && (event.getStageX() >= (Gdx.graphics.getWidth() / 2) + (controlMove.getWidth() / 2)) ) {
                    //handle touch down
                    controlShoot.setCenterPosition(event.getStageX(), event.getStageY());
                    controlShoot.getListeners().get(0).handle(event);
                    controlShoot.pointerID = pointer;

                    stage.addActor(controlShoot);
                }

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(controlMove.pointerID == pointer) {
                    controlMove.pointerID = -1;
                    controlMove.remove();
                    controlMove.getListeners().get(0).handle(event);
                }

                if (controlShoot.pointerID == pointer) {
                    controlShoot.pointerID = -1;
                    controlShoot.remove();
                    controlShoot.getListeners().get(0).handle(event);
                }
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(controlMove.pointerID == pointer )  {
                    controlMove.getListeners().get(0).handle(event);
                } else if(controlShoot.pointerID == pointer) {
                    controlShoot.getListeners().get(0).handle(event);
                }
            }
        });

        Gdx.input.setInputProcessor(stage);

        //make the dodger show up on the screen
        stage.addActor(playerDodge);

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
        if(controlMove.circleActivated) {
            if( (controlMove.circleHypotenuse >= (controlMove.getWidth() / 6)) &&
                    (controlMove.circleHypotenuse <= ((controlMove.getWidth() / 2) + controlMove.padding)) ) {
                //use angle to determine movement
                float move = dt * movementSpeed;

                //time to move by this much based off of the angle
                playerDodge.moveBy((float) Math.cos(controlMove.circleAngle) * move, (float) Math.sin(controlMove.circleAngle) * move);
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
                world.destroyBody(duck.getBody());
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

        world.step(dt, 6, 2);
    }

    @Override
    public void render() {
        stage.draw();

        b2dr.render(world, stage.getCamera().combined);
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

    private void sendPrivateduck() {
        float startX = 0f;
        float startY = 0f;
        float endX = 0f;
        float endY = 0f;

        int sign = rand.nextBoolean() ? 1 : -1;

        //check if start from width or height
        boolean startWidth = rand.nextBoolean();
        if(startWidth) {
            startX = Gdx.graphics.getWidth() * sign;
            startY = rand.nextFloat() * Gdx.graphics.getHeight();
        } else {
            startX = rand.nextFloat() * Gdx.graphics.getWidth();
            startY = Gdx.graphics.getHeight() * sign;
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

        Duck duck = Duck.sendDuck(startX, startY, endX, endY, 100f, game, world);
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
