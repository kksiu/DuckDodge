package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.basetwelve.handlers.StateManager;

/**
 * Created by Kenneth on 8/10/14.
 */
public class MainMenu extends State{
    private TextButton playButton;

    private Skin skin;

    public MainMenu(final StateManager sm) {
        super(sm);

        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        //create the play button
        playButton = new TextButton(" Play! ", skin, "default");
        playButton.setPosition((Gdx.graphics.getWidth() / 2) - (playButton.getWidth() / 2),
                (Gdx.graphics.getHeight() / 2) - (playButton.getHeight() / 2));

        //set listener to change to the play state clicked
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                sm.setState(StateManager.PLAY_ATTACK);
            }
        });

        stage.addActor(playButton);

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
