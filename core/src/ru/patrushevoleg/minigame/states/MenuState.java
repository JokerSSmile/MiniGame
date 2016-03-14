package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Iterator;

import ru.patrushevoleg.minigame.MyGame;
import ru.patrushevoleg.minigame.entities.Particle;
import ru.patrushevoleg.minigame.handlers.ButtonHandler;

public class MenuState extends State {

    private static final Vector2 SCREEN_CENTER = new Vector2(MyGame.WIDTH / 2, MyGame.HEIGHT / 2);
    private static final Rectangle playButtonBounds = new Rectangle(SCREEN_CENTER.x - 40, SCREEN_CENTER.y - 50, 90, 100);
    private static final Color PLAY_BTN_COLOR = new Color(1, 1, 1, 1);

    private ButtonHandler playHandler;
    private ButtonHandler statisticsHandler;
    private ButtonHandler backHandler;
    private Sprite statisticsSprite;

    public MenuState(GameStateManager gsm){
        super(gsm);

        name = "menu";
        playHandler = new ButtonHandler();
        backHandler = new ButtonHandler();
        statisticsHandler = new ButtonHandler();

        Texture statistics = new Texture("statistics.png");
        statisticsSprite = new Sprite(statistics);
        statisticsSprite.setScale(0.12f);
        statisticsSprite.setPosition(SCREEN_SIZE.x - 300, SCREEN_SIZE.y - 300);

        camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void inputHandler() {
        mousePosition = viewport.unproject(mousePosition);

        //play screen
        if (Gdx.input.isTouched() && playButtonBounds.contains(mousePosition.x, mousePosition.y)) {
            playHandler.onClick();
        }
        else if (!Gdx.input.isTouched() && playButtonBounds.contains(mousePosition.x, mousePosition.y)){
            if (playHandler.isOnRelease()) {
                playHandler.onRelease(gsm, new TransitionState(gsm, true, new PlayState(gsm)));
                this.dispose();
            }
        }
        else if (!Gdx.input.isTouched()){
            playHandler.notTouched();
        }

        //statistics screen
        if (Gdx.input.isTouched() && statisticsSprite.getBoundingRectangle().contains(mousePosition.x, mousePosition.y)) {
            statisticsHandler.onClick();
        }
        else if (!Gdx.input.isTouched() && statisticsSprite.getBoundingRectangle().contains(mousePosition.x, mousePosition.y)){
            if (statisticsHandler.isOnRelease()) {
                statisticsHandler.onRelease(gsm, new TransitionState(gsm, true, new StatisticsState(gsm)));
                this.dispose();
            }
        }
        else if (!Gdx.input.isTouched()){
            statisticsHandler.notTouched();
        }

        //exit game
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            backHandler.onClick();
        }
        else if (!Gdx.input.isKeyPressed(Input.Keys.BACK)){
            if (backHandler.isOnRelease()) {
                gsm.isOnExit = true;
                this.dispose();
                backHandler.onRelease();
            }
        }

    }

    @Override
    public void update(float dt) {
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        inputHandler();
        camera.update();

        int randInt = rand.nextInt(20);
        if (randInt == 1){
            particles.add(new Particle(new Vector2(camera.position.x, camera.position.y), SCREEN_SIZE, new Color(0, 0, 0, 1)));
        }

        for (Iterator<Particle> it = particles.iterator(); it.hasNext();){
            Particle particle = it.next();
            if (camera.position.y - SCREEN_SIZE.y / 2 > particle.getPosition().y) {
                it.remove();
            }
        }
        for (Particle particle : particles){
            particle.update(dt);
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        statisticsSprite.draw(batch);
        batch.end();

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (Particle particle : particles){
            particle.draw(shapeRenderer);
        }

        shapeRenderer.setColor(0.8f, 0.8f, 0.8f, 0.8f);
        shapeRenderer.circle(SCREEN_CENTER.x, SCREEN_CENTER.y, 100);
        shapeRenderer.setColor(1f, 1f, 1f, 1f);
        shapeRenderer.triangle(SCREEN_CENTER.x + 60, SCREEN_CENTER.y, SCREEN_CENTER.x - 30,
                SCREEN_CENTER.y + 50, SCREEN_CENTER.x - 30, SCREEN_CENTER.y - 50,
                PLAY_BTN_COLOR, PLAY_BTN_COLOR, PLAY_BTN_COLOR
        );
        shapeRenderer.end();
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
        System.out.println("menu disposed start");
        shapeRenderer.dispose();
        particles.clear();
        System.out.println("menu disposed end");
    }
}
