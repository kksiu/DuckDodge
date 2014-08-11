package com.basetwelve;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.basetwelve.Handlers.StateManager;
import com.basetwelve.Handlers.TextureHandler;

public class MainClass extends ApplicationAdapter {

    //constants (height, width)
    public static final String TITLE = "Duck Dodger";
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    //the state manager to handle all the different states
    StateManager stateManager;
    TextureHandler textureHandler;

	@Override
	public void create () {
        //create the handlers
        stateManager = new StateManager(this);
        textureHandler = new TextureHandler();

        //load all the textures
        loadTextures();
	}

	@Override
	public void render () {

        float dt = Gdx.graphics.getDeltaTime();

        //update the time
        stateManager.update(dt);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render what is currently in the stage manager
        stateManager.render();
	}

    @Override
    public void dispose() {

    }

    public TextureHandler getTextureHandler() {
        return textureHandler;
    }

    private void loadTextures() {
        textureHandler.loadTexture("images/duck_stock.png", "Duck");
    }
}
