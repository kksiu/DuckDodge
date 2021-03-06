package com.basetwelve.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by Kenneth on 8/11/14.
 */
public class TextureHandler {

    private HashMap<String, Texture> textureMap;

    //constructor fo the texture handler
    public TextureHandler() {
        textureMap = new HashMap<String, Texture>();

        //load all teh default textures
        loadTextures();
    }

    //load a texture based on a path and the key
    public void loadTexture(String path, String textureStr) {
        Texture texture  = new Texture(Gdx.files.internal(path));
        textureMap.put(textureStr, texture);
    }

    //get the texture based off of a key
    public Texture getTexture(String textureStr) {
        return textureMap.get(textureStr);
    }

    //dispose texture once given key
    public void disposeTexture(String textureStr) {
        Texture texture = textureMap.remove(textureStr);

        if(texture != null) {
            texture.dispose();
        }
    }

    private void loadTextures() {
        loadTexture("images/duck_stock.png", "Duck");
        loadTexture("images/player_stock.png", "Player");
        loadTexture("images/circle_control.png", "CircleControl");
        loadTexture("images/circle_bullet.png", "Bullet");
    }
}
