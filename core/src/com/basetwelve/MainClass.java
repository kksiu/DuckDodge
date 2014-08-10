package com.basetwelve;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.basetwelve.Handlers.StateManager;

public class MainClass extends ApplicationAdapter {

    //constants (height, width)
    public static final String TITLE = "Duck Dodger";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    //the state manager to handle all the different states
    StateManager stateManager;

	@Override
	public void create () {
        stateManager = new StateManager(this);
	}

	@Override
	public void render () {

        float dt = Gdx.graphics.getDeltaTime();

        //update the time
        stateManager.update(dt);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateManager.render();
	}

    @Override
    public void dispose() {

    }
}
