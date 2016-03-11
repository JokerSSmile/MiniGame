package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TransitionState extends State{

    private float r;
    private float g;
    private float b;
    private float a;
    private boolean isStartBlack;
    private State state;

    public TransitionState(GameStateManager gsm, boolean isStartBlack, State state){
        super(gsm);
        this.isStartBlack = isStartBlack;
        this.state = state;
        if (!isStartBlack){
            r = 0;
            g = 0;
            b = 0;
            a = 0;
        }
        else{
            r = 1;
            g = 1;
            b = 1;
            a = 0;
        }
    }

    @Override
    protected void inputHandler() {

    }

    @Override
    protected void update(float dt) {
        inputHandler();
        if (!isStartBlack) {
            if (r < 1) {
                r += dt * 2;
                g += dt * 2;
                b += dt * 2;
                a += dt * 2;
                if (gsm.music.getVolume() > 0.3f) {
                    gsm.music.setVolume(gsm.music.getVolume() - dt);
                }
            } else {
                gsm.set(state);
            }
        }
        else{
            if (r > 0) {
                r -= dt * 2;
                g -= dt * 2;
                b -= dt * 2;
                a -= dt * 2;
                if (gsm.music.getVolume() < 1f && state.name.equals("play")) {
                    gsm.music.setVolume(gsm.music.getVolume() + dt);
                }
            } else {
                gsm.set(state);
            }
        }
    }

    @Override
    protected void render(SpriteBatch batch) {
        Gdx.gl.glClearColor(r, g, b, a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
