package com.basetwelve.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
    private TextButton settingsButton;
    private TextButton instrButton;

    private Skin skin;

    private float buttonScaleX = 3.5f;
    private float buttonScaleY = 1.5f;


    //Title variables
    private BitmapFont font;
    private final String TITLE = "Duck Dodger!";
    private float TITLE_WIDTH;
    private float TITLE_HEIGHT;

    //set padding
    private float PADDING;


    public MainMenu(final StateManager sm) {
        super(sm);

        //create PADDING
        PADDING = Gdx.graphics.getHeight() / 7;

        //create font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Kenney_Pixel.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);

        //set title height and width
        TITLE_WIDTH = (Gdx.graphics.getWidth() / 2) - (font.getBounds(TITLE).width / 2);
        TITLE_HEIGHT = (Gdx.graphics.getHeight()) - PADDING;

        //create skin
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        //create the play button
        playButton = new TextButton("Play!", skin, "default");
        playButton.setWidth(buttonScaleX * playButton.getWidth());
        playButton.setHeight(buttonScaleY * playButton.getHeight());
        playButton.getStyle().font.setScale(buttonScaleY);
        playButton.setPosition((Gdx.graphics.getWidth() / 2) - (playButton.getWidth() / 2),
                TITLE_HEIGHT - font.getBounds(TITLE).height - (PADDING * 2));

        //set listener to change to the play state clicked
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                sm.setState(StateManager.PLAY_ATTACK);
            }
        });

        //create the settings button
        settingsButton = new TextButton("Settings", skin, "default");
        settingsButton.setWidth(playButton.getWidth());
        settingsButton.setHeight(playButton.getHeight());
        settingsButton.getStyle().font.setScale(buttonScaleY);
        settingsButton.setPosition((Gdx.graphics.getWidth() / 2) - (settingsButton.getWidth() / 2),
                playButton.getY() - PADDING);

        //create instructions button
        instrButton = new TextButton("Instructions", skin, "default");
        instrButton.setWidth(playButton.getWidth());
        instrButton.setHeight(playButton.getHeight());
        instrButton.getStyle().font.setScale(buttonScaleY);
        instrButton.setPosition((Gdx.graphics.getWidth() / 2) - (instrButton.getWidth() / 2),
                settingsButton.getY() - PADDING);

        //add buttons to stage
        stage.addActor(playButton);
        stage.addActor(settingsButton);
        stage.addActor(instrButton);

        //set the input processor to this stage
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

        stage.getBatch().begin();
        font.draw(stage.getBatch(), TITLE, TITLE_WIDTH, TITLE_HEIGHT);
        stage.getBatch().end();
    }

    @Override
    public void dispose() {

    }
}
