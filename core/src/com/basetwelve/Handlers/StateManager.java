package com.basetwelve.handlers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.basetwelve.MainClass;
import com.basetwelve.state.MainMenu;
import com.basetwelve.state.PlayAttack;
import com.basetwelve.state.PlayDodge;
import com.basetwelve.state.State;

import java.util.Stack;

/**
 * Created by Kenneth on 8/10/14.
 */
public class StateManager {
    //hold the current application listener
    private MainClass game;

    //stack containing all the states
    private Stack<State> stateStack;

    public static final int MAIN_MENU = 0;
    public static final int PLAY_DODGE = 1;
    public static final int PLAY_ATTACK = 2;

    //constructor
    public StateManager(MainClass game) {

        this.game = game;

        stateStack = new Stack<State>();

        //start with the main menu
        pushState(PLAY_DODGE);
    }

    //returns the current applicaiton listener
    public MainClass getGame() { return game; }

    //update only the top state in the stack
    public void update(float dt) {
        stateStack.peek().update(dt);
    }

    //render all states in the stack
    public void render() {
        //in java, this will go from bottom to the top
        for(State state : stateStack) {
            state.render();
        }
    }

    private State getState(int state) {

        //find the state to get
        if(state == MAIN_MENU) {
            return new MainMenu(this);
        } else if(state == PLAY_DODGE) {
            return new PlayDodge(this);
        } else if(state == PLAY_ATTACK) {
            return new PlayAttack(this);
        }

        return null;
    }

    public void setState(int state) {
        popState();
        pushState(state);
    }

    public void pushState(int state) {
        stateStack.push(getState(state));
    }

    public void popState() {
        State state = stateStack.pop();
        state.dispose();
    }
}
