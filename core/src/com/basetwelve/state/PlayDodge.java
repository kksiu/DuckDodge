package com.basetwelve.state;

import com.basetwelve.Handlers.StateManager;

/**
 * Created by Kenneth on 8/11/14.
 */
public class PlayDodge extends State {

    public PlayDodge(StateManager sm) {
        super(sm);
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
