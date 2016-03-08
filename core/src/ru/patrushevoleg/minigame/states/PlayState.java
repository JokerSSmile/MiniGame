package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Iterator;
import java.util.Vector;

import ru.patrushevoleg.minigame.entities.Particle;
import ru.patrushevoleg.minigame.entities.PlayerBox;
import ru.patrushevoleg.minigame.entities.Text;
import ru.patrushevoleg.minigame.entities.Wall;
import ru.patrushevoleg.minigame.handlers.ButtonHandler;

public class PlayState extends State {

    private static final Color PAUSE_COLOR = new Color(0, 0, 0, 0.5f);
    private static final Color HEADER_COLOR = new Color(1, 1, 1, 0.5f);
    private static final Vector2 START_POSITION = new Vector2(50, 100);
    private static final Vector2 PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS = new Vector2(70, 70);
    private static final Vector2 PAUSE_BTN_SIZE = new Vector2(20, 50);
    private static final int SHIFT_BEETVEN_PAUSE_RECTS = 30;
    private static final String PREFS_NAME = "myprefs";

    public enum CurrentState { RUNNING, PAUSED }
    private PlayerBox playerBox;
    private Vector<Wall> walls;
    private ButtonHandler handler;
    private ButtonHandler backHandler;
    private Rectangle pauseButtonBounds;
    private String scoreString;
    private Text text;
    private Vector3 cameraPosition;
    private Preferences prefs;
    private float timer;

    public CurrentState gameState = CurrentState.RUNNING;

    public PlayState(GameStateManager gsm){
        super(gsm);

        name = "play";
        cameraPosition = new Vector3();
        playerBox = new PlayerBox(START_POSITION, SCREEN_SIZE);
        handler = new ButtonHandler();
        backHandler = new ButtonHandler();
        scoreString = "";
        text = new Text(gsm.generator, 57, true);
        prefs = Gdx.app.getPreferences(PREFS_NAME);

        walls = new Vector<Wall>();
        walls.add(new Wall((int) SCREEN_SIZE.y));
        walls.add(new Wall((int) SCREEN_SIZE.y + (int) SCREEN_SIZE.y / 2));

        camera.setToOrtho(false, SCREEN_SIZE.x, SCREEN_SIZE.y);
        pauseButtonBounds = new Rectangle();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        gsm.music.setVolume(1);
        if (!gsm.music.isPlaying()){
            gsm.music.play();
        }
    }

    @Override
    protected void inputHandler() {

        mousePosition = viewport.unproject(mousePosition);

        if (Gdx.input.isTouched())
        {
            if (pauseButtonBounds.contains(mousePosition.x, mousePosition.y)){
                handler.onClick();
            }
            else {
                playerBox.move(mousePosition);
            }
        }
        else
        {
            if (pauseButtonBounds.contains(mousePosition.x, mousePosition.y)){
                handler.onRelease(this, gsm.music);
            }
            else {
                playerBox.notMove();
                handler.notTouched();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.BACK)){
            backHandler.onClick();
        }

        else if (!Gdx.input.isKeyPressed(Input.Keys.BACK)){
            if (backHandler.isOnRelease()) {
                backHandler.onRelease(this, gsm.music);
            }
        }
    }

    @Override
    public void update(float dt) {

        cameraPosition = camera.position;
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        pauseButtonBounds.set((cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x, cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y,
                SHIFT_BEETVEN_PAUSE_RECTS + PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);
        inputHandler();

        switch (gameState){
            case RUNNING:
                playerBox.update(dt);
                camera.position.y = playerBox.getPosition().y + SCREEN_SIZE.y / 2 - START_POSITION.y;
                camera.update();

                timer += dt;

                int randInt = rand.nextInt(10);
                if (randInt == 1){
                    particles.add(new Particle(new Vector2(cameraPosition.x, cameraPosition.y), SCREEN_SIZE, new Color(1, 1, 1, 1)));
                }

                for (Wall wall : walls) {
                    if (cameraPosition.y - SCREEN_SIZE.y / 2 - wall.getThickness() > wall.getPosition().y) {
                        wall.reposition((int) (cameraPosition.y + SCREEN_SIZE.y / 2));
                    }
                    if (wall.isCollides(playerBox.getBounds())) {
                        prefs.putInteger("currentscore", (int)playerBox.getPosition().y);
                        prefs.putInteger("timeplayed", (int)timer + prefs.getInteger("timeplayed"));
                        prefs.flush();
                        gsm.set(new TransitionState(gsm, false, new EndGameState(gsm)));
                        this.dispose();
                    }
                }

                for (Iterator<Particle> it = particles.iterator(); it.hasNext();){
                    Particle particle = it.next();
                    if (cameraPosition.y - SCREEN_SIZE.y / 2 > particle.getPosition().y) {
                        it.remove();
                    }
                }
                for (Particle particle : particles){
                    particle.update(dt);
                }
            case PAUSED:

        }
        scoreString = "" + (int)playerBox.getPosition().y;
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        text.draw(batch, scoreString, new Vector2(25, cameraPosition.y + SCREEN_SIZE.y / 2 - 20));
        batch.end();

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        playerBox.render(shapeRenderer);                                                                                                                //player

        for (Particle particle : particles){
            particle.draw(shapeRenderer);
        }

        Color playersColor = playerBox.getColor();
        for (Wall wall : walls) {
            shapeRenderer.rect(wall.getPosition().x, wall.getPosition().y, wall.getSize().x, wall.getSize().y,
                    playersColor, playersColor, playersColor, playersColor);
        }

        shapeRenderer.rect(0, camera.position.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SIZE.y - 40,                                                             //header
                SCREEN_SIZE.x, PAUSE_BTN_SIZE.y + 40, HEADER_COLOR, HEADER_COLOR, HEADER_COLOR, HEADER_COLOR);

        switch (gameState) {
            case RUNNING:
                shapeRenderer.rect((cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x, cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y, PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);              //pause
                shapeRenderer.rect((cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x + SHIFT_BEETVEN_PAUSE_RECTS,
                        cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y, PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);              //pause
                shapeRenderer.end();
                return;
            case PAUSED:
                shapeRenderer.rect(cameraPosition.x - SCREEN_SIZE.x / 2, cameraPosition.y - SCREEN_SIZE.y / 2,                                          //screen darker when paused
                        SCREEN_SIZE.x, SCREEN_SIZE.y - (PAUSE_BTN_SIZE.y + 40), PAUSE_COLOR, PAUSE_COLOR, PAUSE_COLOR, PAUSE_COLOR);
                shapeRenderer.triangle((cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x, cameraPosition.y + SCREEN_SIZE.y / 2 - 20,       //resume
                        (cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x, cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SIZE.y - 20,
                        (cameraPosition.x) * 2 - 20, cameraPosition.y + SCREEN_SIZE.y / 2 - 45);
                shapeRenderer.end();
        }
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
        gameState = CurrentState.PAUSED;
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playerBox.dispose();
        shapeRenderer.dispose();
        text.dispose();
        for (Wall wall : walls){
            wall.dispose();
        }
         particles.clear();
         text.dispose();
    }
}
