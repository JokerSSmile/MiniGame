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

public class EndGameState extends State {

    private static final String PREFS_NAME = "myprefs";
    private static final String REFRESH_TEXTURE_PATH = "refresh.png";
    private static final String HOME_TEXTURE_PATH = "home.png";

    private ButtonHandler repeatHandler;
    private ButtonHandler backHandler;
    private ButtonHandler menuHandler;

    private Sprite repeatSprite;
    private Sprite menuSprite;
    private Text bigText;
    private Text normalText;
    private Text smallText;
    private Preferences prefs;
    private boolean isHighScore = false;

    public EndGameState(GameStateManager gsm){
        super(gsm);
        name = "endgame";

        repeatHandler = new ButtonHandler();
        backHandler = new ButtonHandler();
        menuHandler = new ButtonHandler();

        SCREEN_SIZE = new Vector2(MyGame.WIDTH, MyGame.HEIGHT);
        bigText = new Text(gsm.generator, 100, false);
        normalText = new Text(gsm.generator, 60, false);
        smallText = new Text(gsm.generator, 30, false);

        Texture repeat = new Texture(REFRESH_TEXTURE_PATH);
        repeatSprite = new Sprite(repeat);
        repeatSprite.setScale(0.12f);
        repeatSprite.setPosition(SCREEN_SIZE.x - 300, SCREEN_SIZE.y - 300);

        Texture menu = new Texture(HOME_TEXTURE_PATH);
        menuSprite = new Sprite(menu);
        menuSprite.setScale(0.12f);
        menuSprite.setPosition(-SCREEN_SIZE.x + 275, SCREEN_SIZE.y - 300);

        prefs = Gdx.app.getPreferences(PREFS_NAME);
        prefs.putInteger("gamesplayed", prefs.getInteger("gamesplayed") + 1);
        if (prefs.getInteger("currentscore") > prefs.getInteger("highscore")) {
            prefs.putInteger("highscore", prefs.getInteger("currentscore"));
            prefs.flush();
            isHighScore = true;
        }

        camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void inputHandler() {
        mousePosition = viewport.unproject(mousePosition);

        Rectangle repeatSpriteRect = repeatSprite.getBoundingRectangle();
        Rectangle menuSpriteRect = menuSprite.getBoundingRectangle();

        if (Gdx.input.isTouched()){
            if (repeatSpriteRect.contains(mousePosition.x, mousePosition.y)){
                repeatHandler.onClick();
            }
            else if (menuSpriteRect.contains(mousePosition.x, mousePosition.y)){
                menuHandler.onClick();
            }
        }
        else{
            if (repeatSpriteRect.contains(mousePosition.x, mousePosition.y)){
                if (repeatHandler.isOnRelease()) {
                    repeatHandler.onRelease(gsm, new TransitionState(gsm, true, new PlayState(gsm)));
                    this.dispose();
                }
            }
            else if (menuSpriteRect.contains(mousePosition.x, mousePosition.y)){
                if (menuHandler.isOnRelease()) {
                    gsm.set(new MenuState(gsm));
                    this.dispose();
                }
            }
            else {
                menuHandler.notTouched();
                repeatHandler.notTouched();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            backHandler.onClick();
        }

        else if (!Gdx.input.isKeyPressed(Input.Keys.BACK)){
            if (backHandler.isOnRelease()) {
                backHandler.onRelease(gsm, new MenuState(gsm));
                this.dispose();
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
            if (!isHighScore) {
                particles.add(new Particle(new Vector2(camera.position.x, camera.position.y), SCREEN_SIZE, new Color(0, 0, 0, 1)));
            }
            else{
                particles.add(new Particle(new Vector2(camera.position.x, camera.position.y), SCREEN_SIZE, new Color(1, 0, 0, 1)));
            }
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

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        batch.begin();
        repeatSprite.draw(batch);
        menuSprite.draw(batch);

        if (isHighScore){
            normalText.draw(batch, "new high", new Vector2(25, SCREEN_SIZE.y / 2 + 205));
            normalText.draw(batch, "score!", new Vector2(25, SCREEN_SIZE.y / 2 + 150));

        }
        else {
            normalText.draw(batch, "score:", new Vector2(25, SCREEN_SIZE.y / 2 + 150));
        }
        bigText.draw(batch, prefs.getInteger("currentscore"), new Vector2(25, SCREEN_SIZE.y / 2 + 80));
        if (!isHighScore) {
            smallText.draw(batch, "high score:", new Vector2(50, SCREEN_SIZE.y / 2 - 165));
            smallText.draw(batch, prefs.getInteger("highscore"), new Vector2(50, SCREEN_SIZE.y / 2 - 200));
        }
        batch.end();

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
