package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {

    private Stack<State> states;
    private static String stateName;
    public Music music;
    public boolean isOnExit;

    public GameStateManager(){
        states = new Stack<State>();
        music = Gdx.audio.newMusic(Gdx.files.internal("BigCarTheft.mp3"));
        music.setLooping(true);
        stateName = new String("");
        isOnExit = false;
    }

    public void push(State state){
        states.push(state);
    }

    public void pop() {
        states.pop().dispose();
        stateName = states.peek().name;
    }

    public void set(State state){
        states.pop();
        states.push(state);
        stateName = states.peek().name;
    }

    public void pause(){
        if (states.size() > 0) {
            states.peek().pause();
        }
    }

    public void update(float dt){
        System.out.println(states.size());
        states.peek().update(dt);
    }

    public void render(SpriteBatch batch){
        states.peek().render(batch);
    }

    public String getStateName(){
        return stateName;
    }

    public void dispose(){
        states.clear();
        music.dispose();
    }
}
