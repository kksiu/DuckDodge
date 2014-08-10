package com.basetwelve.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.basetwelve.Handlers.StateManager;
import com.basetwelve.MainClass;

/**
 * Created by Kenneth on 8/10/14.
 */
public abstract class State {
    protected StateManager sm;
    protected MainClass game;

    protected Stage stage;

    protected State(StateManager sm) {
        this.sm = sm;
        game = sm.getGame();
        stage = new Stage();
    }

    public abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
