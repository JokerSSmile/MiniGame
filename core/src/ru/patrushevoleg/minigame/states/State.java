package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State {

    protected OrthographicCamera camera;
    protected Vector3 mousePosition;
    protected GameStateManager gsm;

    protected State(GameStateManager gsm){
        this.gsm = gsm;
        camera = new OrthographicCamera();
        mousePosition = new Vector3();
    }

    protected abstract void inputHandle();

    public abstract void update(float dp);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();

}
