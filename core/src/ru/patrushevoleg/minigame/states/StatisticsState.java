package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;

import java.util.Iterator;

import ru.patrushevoleg.minigame.MyGame;
import ru.patrushevoleg.minigame.entities.Particle;
import ru.patrushevoleg.minigame.entities.Text;
import ru.patrushevoleg.minigame.handlers.ButtonHandler;

public class StatisticsState extends State {

    private static final String PREFS_NAME = "ru.patrushevoleg.blocks";

    public enum CurrentState { NORMAL, CONFIRM_RESET }

    private ButtonHandler backScreenHandler;
    private ButtonHandler backHandler;
    private ButtonHandler resetHandler;
    private ButtonHandler confirmResetHandler;
    private ButtonHandler declineResetHandler;

    private Sprite menuSprite;
    private Sprite resetSprite;

    private static Text bigText = new Text(70, true);
    private static Text normalText = new Text(45, true);
    private static Text smallText = new Text(30, true);
    private static Text yesText = new Text(45, true);
    private static Text noText = new Text(45, true);
    private Preferences prefs;

    public CurrentState gameState = CurrentState.NORMAL;

    public StatisticsState(GameStateManager gsm){
        super(gsm);

        name = "state";
        backScreenHandler = new ButtonHandler();
        backHandler = new ButtonHandler();
        resetHandler = new ButtonHandler();
        confirmResetHandler = new ButtonHandler();
        declineResetHandler = new ButtonHandler();
        SCREEN_SIZE = new Vector2(MyGame.WIDTH, MyGame.HEIGHT);

        Texture back = new Texture("home_white.png");
        menuSprite = new Sprite(back);
        menuSprite.setScale(0.12f);
        menuSprite.setPosition(SCREEN_SIZE.x - 300, SCREEN_SIZE.y - 300);

        Texture reset = new Texture("refresh_white.png");
        resetSprite = new Sprite(reset);
        resetSprite.setScale(0.12f);
        resetSprite.setPosition(-SCREEN_SIZE.x + 275, SCREEN_SIZE.y - 300);

        prefs = Gdx.app.getPreferences(PREFS_NAME);
        camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void inputHandler() {
        mousePosition = viewport.unproject(mousePosition);

        Rectangle menuSpriteRect = menuSprite.getBoundingRectangle();
        Rectangle resetSpriteRect = resetSprite.getBoundingRectangle();
        Rectangle yesTextRect = yesText.getBounds();
        Rectangle noTextRect = noText.getBounds();

        if (Gdx.input.isTouched() && menuSpriteRect.contains(mousePosition.x, mousePosition.y)) {
            backScreenHandler.onClick();
        }
        else if (!Gdx.input.isTouched() && menuSpriteRect.contains(mousePosition.x, mousePosition.y)){
            if (backScreenHandler.isOnRelease()) {
                backScreenHandler.onRelease(gsm, new TransitionState(gsm, false, new MenuState(gsm)));
                this.dispose();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            backHandler.onClick();
        }
        else if (!Gdx.input.isKeyPressed(Input.Keys.BACK)){
            if (gameState == CurrentState.NORMAL) {
                if (backHandler.isOnRelease()) {
                    backHandler.onRelease(gsm, new TransitionState(gsm, false, new MenuState(gsm)));
                    this.dispose();
                }
            }
            else{
                backHandler.onRelease(this);
            }
        }

        if (gameState == CurrentState.NORMAL) {
            if (Gdx.input.isTouched() && resetSpriteRect.contains(mousePosition.x, mousePosition.y)) {
                resetHandler.onClick();
            } else if (!Gdx.input.isTouched() && resetSpriteRect.contains(mousePosition.x, mousePosition.y)) {
                resetHandler.onRelease(this);
            }
        }
        else {
            if (Gdx.input.isTouched() && yesTextRect.contains(mousePosition.x, mousePosition.y)) {
                confirmResetHandler.onClick();
            } else if (!Gdx.input.isTouched() && yesTextRect.contains(mousePosition.x, mousePosition.y)) {
                if (confirmResetHandler.isOnRelease()) {
                    gameState = CurrentState.NORMAL;
                    prefs.clear();
                }
            }

            if (Gdx.input.isTouched() && noTextRect.contains(mousePosition.x, mousePosition.y)) {
                declineResetHandler.onClick();
            } else if (!Gdx.input.isTouched() && noTextRect.contains(mousePosition.x, mousePosition.y)) {
                if (declineResetHandler.isOnRelease()) {
                    gameState = CurrentState.NORMAL;
                }
            }

            if (!Gdx.input.isTouched()){
                declineResetHandler.notTouched();
                confirmResetHandler.notTouched();
                resetHandler.notTouched();
                backScreenHandler.notTouched();
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
            particles.add(new Particle(new Vector2(camera.position.x, camera.position.y), SCREEN_SIZE, new Color(1, 1, 1, 1)));
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

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        menuSprite.draw(batch);

        if (gameState == CurrentState.NORMAL) {
            resetSprite.draw(batch);
            normalText.draw(batch, "high score:", new Vector2(25, SCREEN_SIZE.y / 2 + 200));
            smallText.draw(batch, prefs.getInteger("highscore"), new Vector2(25, SCREEN_SIZE.y / 2 + 150));
            normalText.draw(batch, "games played:", new Vector2(25, SCREEN_SIZE.y / 2 + 100));
            smallText.draw(batch, prefs.getInteger("gamesplayed"), new Vector2(25, SCREEN_SIZE.y / 2 + 50));
            normalText.draw(batch, "time played:", new Vector2(25, SCREEN_SIZE.y / 2));
            smallText.draw(batch, prefs.getInteger("timeplayed") + " sec", new Vector2(25, SCREEN_SIZE.y / 2 - 50));
        }
        else {
            bigText.draw(batch, "reset data?", new Vector2(25, SCREEN_SIZE.y / 2 + 100));
            yesText.draw(batch, "yes", new Vector2(25, SCREEN_SIZE.y / 2));
            noText.draw(batch, "no", new Vector2(25, SCREEN_SIZE.y / 2 - 70));
        }

        prefs.flush();
        batch.end();

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Particle particle : particles){
            particle.draw(shapeRenderer);
        }

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
        shapeRenderer.dispose();
        bigText.dispose();
        normalText.dispose();
        smallText.dispose();
    }
}
