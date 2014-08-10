package com.basetwelve.state;

import com.basetwelve.Handlers.StateManager;

/**
 * Created by Kenneth on 8/10/14.
 */
public class MainMenu extends State{
    public MainMenu(StateManager sm) {
        super(sm);

    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {


        //have the stage act
        stage.act(dt);
        stage.draw();
    }

    @Override
    public void render() {
    }

    @Override
    public void dispose() {

    }
}
