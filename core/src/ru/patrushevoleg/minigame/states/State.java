package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;
import java.util.Vector;

import ru.patrushevoleg.minigame.MyGame;
import ru.patrushevoleg.minigame.entities.Particle;

public abstract class State implements Screen {

    public static String name;

    protected Vector2 SCREEN_SIZE;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Vector3 mousePosition;
    protected GameStateManager gsm;
    protected Vector<Particle> particles;
    protected ShapeRenderer shapeRenderer;
    protected Random rand;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;
        camera = new OrthographicCamera();
        viewport = new FitViewport(MyGame.WIDTH, MyGame.HEIGHT, camera);
        mousePosition = new Vector3();
        particles = new java.util.Vector<Particle>();
        SCREEN_SIZE = new Vector2 (MyGame.WIDTH, MyGame.HEIGHT);
        shapeRenderer = new ShapeRenderer();
        rand = new Random();
    }

    protected abstract void inputHandler();

    protected abstract void update(float dt);

    protected abstract void render(SpriteBatch batch);

    public abstract void dispose();
}
