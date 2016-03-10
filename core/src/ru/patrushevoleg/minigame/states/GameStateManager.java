package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.Stack;

public class GameStateManager {

    private Stack<State> states;
    public Music music;

    public GameStateManager(){
        states = new Stack<State>();
        music = Gdx.audio.newMusic(Gdx.files.internal("BigCarTheft.mp3"));
        music.setLooping(true);
    }

    public void push(State state){
        states.push(state);
    }

    public void pop()
    {
        states.pop().dispose();
    }

    public void set(State state){
        states.pop();
        states.push(state);
    }

    public void pause(){
        states.peek().pause();
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(SpriteBatch batch){
        states.peek().render(batch);
    }

    public void dispose(){
        states.clear();
        music.dispose();
    }
}
