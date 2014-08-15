package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.basetwelve.entities.*;
import com.basetwelve.handlers.PlayCollisionHandler;
import com.basetwelve.handlers.StateManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;


/**
 * Created by Kenneth on 8/11/14.
 */
public class PlayDodge extends State {

    public final short BULLET_ACTOR = 1;
    public final short DUCK_ACTOR = 2;
    public final short PLAYER_ACTOR = 4;

    //player dodge variables
    private Dodger playerDodge;

    //control variables
    private CircleControl controlMove;

    //circle for shooting
    private CircleControl controlShoot;

    //shooting timer
    private Timer shootTimer;
    private float shootRepeat = 0.05f;
    private float shootSpeed = 300f;
    private Timer.Task shootTask;

    //list of bullets
    List<Bullet> bulletList;

    //animation for shooting
    TextureAtlas shootAtlas;

    //padding for the circle
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
    private float duckRepeat = 0.5f;
    private Timer.Task duckTask;

    //Box2dWorld
    World world;
    Box2DDebugRenderer b2dr;

    //collision handler for box2d
    PlayCollisionHandler colHandler;

    //need a list of explosions
    List<AnimatedBox2dDActor> explosionList;

    public PlayDodge(StateManager sm) {
        super(sm);

        //add the shooting texture atlas
        shootAtlas =  new TextureAtlas(Gdx.files.internal("images/explosion/explosion.atlas"));

        //create collision handler
        colHandler = new PlayCollisionHandler();

        //create box2D world (don't need physics)
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(colHandler);
        b2dr = new Box2DDebugRenderer();

        //initialize random
        rand = new Random();

        //list of ducks
        duckList = new ArrayList<Duck>();

        //create the bullet list
        bulletList = new ArrayList<Bullet>();

        //list of explosions
        explosionList = new ArrayList<AnimatedBox2dDActor>();

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
        playerDodge.setBody(PLAYER_ACTOR, DUCK_ACTOR);

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

                    //shooting timer
                    shootTimer = new Timer();
                    shootTimer.scheduleTask(shootTask, 0.0f, shootRepeat);

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
                    shootTimer.stop();
                    shootTask.cancel();
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
        duckTimer.scheduleTask(duckTask, 0.0f, duckRepeat);

        //shooting mechanics
        shootTask = new Timer.Task() {
            @Override
            public void run() {
                sendBullet();
            }
        };
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {

        //check if explosions are done
        for(int i = 0; i < explosionList.size(); i++) {
            AnimatedBox2dDActor actor = explosionList.get(i);

            //if the explosion is done, remove from the stage
            if(actor.getAnimation().isAnimationFinished(actor.getStateTime())) {
                explosionList.remove(actor);
                actor.remove();
                i--;
            }
        }

        //see if there are ducks to remove and also add the explosions at the points
        removeActors(colHandler.getActorsToRemoveFromDodger());
        removeActors(colHandler.getActorsToRemoveFromBullet());

        //add the explosions
        removePointsAndAddExplosion(colHandler.getPointsToExplodeFromDodger());

        //add points before showing the points to explode
        playerDodge.addScore(colHandler.getPointsToExplodeFromBullet().size());
        removePointsAndAddExplosion(colHandler.getPointsToExplodeFromBullet());

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
        //removeOutOfBoundsActors(duckList);
        for(int i = 0; i < duckList.size(); i++) {
            Duck duck = duckList.get(i);

            if(duck.getActions().size <= 1) {
                duckList.remove(duck);
                duck.removeBody();
                duck.remove();
                i--;
            }
        }

        //remove bullets that are out of bound
        removeOutOfBoundsActors(bulletList);

        //have the stage act
        world.step(dt, 6, 2);
        stage.act(dt);
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

    //this will send random ducks everywhere
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

        Duck duck = new Duck(game, world);
        duck.sendActor(startX, startY, endX, endY, 100f, DUCK_ACTOR, (short)(BULLET_ACTOR | PLAYER_ACTOR));
        RepeatAction repeatAction = new RepeatAction();
        repeatAction.setAction(parallel(rotateBy(360, 1.0f)));
        repeatAction.setCount(RepeatAction.FOREVER);
        duck.addAction(repeatAction);

        duckList.add(duck);
        stage.addActor(duck);
        duck.toBack();
    }

    //this will send bullets based off of the second control circle
    public void sendBullet() {
        if(controlShoot.circleActivated) {
            //shoot in the direction that the finger is pointed (based off of angle)

            Texture bTexture = game.getTextureHandler().getTexture("Bullet");
            Bullet nBullet = new Bullet(new TextureRegion(bTexture, bTexture.getWidth(), bTexture.getHeight()), world);
            float scaling = 1/4f;
            nBullet.setWidth(scaling * nBullet.getWidth());
            nBullet.setHeight(scaling * nBullet.getHeight());
            nBullet.setScaling(Scaling.fill);
            nBullet.setBody(BULLET_ACTOR, DUCK_ACTOR);

            //send bullet
            nBullet.sendActorAngle((float) Math.toDegrees(controlShoot.circleAngle), playerDodge, shootSpeed);

            //add to stage
            stage.addActor(nBullet);

            //add to bullet list
            bulletList.add(nBullet);
        }
    }

    //remove all the actors properly
    public void removeActors(List<Box2DActor> listActors) {
        while(listActors.size() > 0) {
            Box2DActor actor = listActors.get(0);

            if(actor.getClass().equals(Duck.class)) {
                duckList.remove(actor);
            } else if(actor.getClass().equals(Bullet.class)) {
                bulletList.remove(actor);
            }

            actor.removeBody();
            actor.remove();
            listActors.remove(actor);
        }
    }

    public void removePointsAndAddExplosion(List<Vector2> listPoints) {
        while(listPoints.size() > 0) {
            //now add explosion here
            Vector2 pointToExplode = listPoints.get(0);
            //now make an explosion
            AnimatedBox2dDActor explode = new AnimatedBox2dDActor(new Animation(1/15f, shootAtlas.getRegions()), world, false);
            explode.setCenterPosition(pointToExplode.x, pointToExplode.y);
            stage.addActor(explode);
            explosionList.add(explode);

            //remove from points to explode
            listPoints.remove(0);
        }
    }

    public void removeOutOfBoundsActors(List<? extends Box2DActor> actorList) {
        for(int i = 0; i < actorList.size(); i++) {
            Box2DActor actor = actorList.get(i);
            //if all actions are done (i.e., off the screen), remove
            if(actor.isOutOfBounds()) {
                actorList.remove(actor);
                actor.removeBody();
                actor.remove();
                i--;
            }
        }
    }
}
