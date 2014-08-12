package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Scaling;
import com.basetwelve.entities.Dodger;
import com.basetwelve.handlers.StateManager;

/**
 * Created by Kenneth on 8/11/14.
 */
public class PlayDodge extends State {
    private Dodger playerDodge;


    public PlayDodge(StateManager sm) {
        super(sm);

        //make a texture region based off of a person
        TextureRegion texReg = new TextureRegion(game.getTextureHandler().getTexture("Player"), 17, 21);

        //now make a dodger
        playerDodge = new Dodger(texReg);

        float scale = 2.0f;

        playerDodge.setWidth(scale * playerDodge.getWidth());
        playerDodge.setHeight(scale * playerDodge.getHeight());
        playerDodge.setScaling(Scaling.fill);

        //set in center
        playerDodge.setCenterPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);



        //make the dodger show up on the screen
        stage.addActor(playerDodge);
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
        stage.dispose();
    }
}
