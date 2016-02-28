package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.patrushevoleg.minigame.MyGame;

public abstract class State {

    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Vector3 mousePosition;
    protected GameStateManager gsm;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, camera);
        mousePosition = new Vector3();
    }

    protected abstract void inputHandle();

    protected abstract void update(float dp);

    protected abstract void render(SpriteBatch batch);

    protected abstract void dispose();
}
