package com.basetwelve.Handlers;

import com.basetwelve.MainClass;
import com.basetwelve.state.MainMenu;
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

    //constructor
    public StateManager(MainClass game) {
        this.game = game;
        stateStack = new Stack<State>();

        //start with the main menu
        pushState(MAIN_MENU);
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
        if(state == MAIN_MENU) {
            return new MainMenu(this);
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
