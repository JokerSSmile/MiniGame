package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import ru.patrushevoleg.minigame.MyGame;
import ru.patrushevoleg.minigame.entities.Particle;
import ru.patrushevoleg.minigame.entities.PlayerBox;
import ru.patrushevoleg.minigame.entities.Text;
import ru.patrushevoleg.minigame.entities.Wall;
import ru.patrushevoleg.minigame.handlers.ButtonHandler;

public class PlayState extends State {

    private static final Color PAUSE_COLOR = new Color(0, 0, 0, 0.5f);
    private static final Color HEADER_COLOR = new Color(1, 1, 1, 0.5f);

    private static final Vector2 START_POSITION = new Vector2(50, 50);
    private static final Vector2 PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS = new Vector2(70, 70);
    private static final Vector2 PAUSE_BTN_SIZE = new Vector2(20, 50);
    private static final int SHIFT_BEETVEN_PAUSE_RECTS = 30;

    public enum CurrentState { RUNNING, PAUSED }
    private ShapeRenderer shapeRenderer;
    private PlayerBox playerBox;
    private Vector<Wall> walls;
    private Vector <Particle> particles;
    private ButtonHandler handler;
    private Rectangle pauseButtonBounds;
    private String scoreString;
    private Text text;
    private Random rand;
    private Vector3 cameraPosition;
    private Vector2 SCREEN_SIZE;

    public CurrentState gameState = CurrentState.RUNNING;

    public PlayState(GameStateManager gsm){
        super(gsm);
        SCREEN_SIZE = new Vector2(MyGame.WIDTH, MyGame.HEIGHT);
        cameraPosition = new Vector3();
        playerBox = new PlayerBox(START_POSITION);
        rand = new Random();
        shapeRenderer = new ShapeRenderer();
        handler = new ButtonHandler();
        scoreString = "";
        text = new Text(57);

        walls = new Vector<Wall>();
        walls.add(new Wall((int)SCREEN_SIZE.y));
        walls.add(new Wall((int)SCREEN_SIZE.y + (int)SCREEN_SIZE.y / 2));

        particles = new Vector<Particle>();

        camera.setToOrtho(false, SCREEN_SIZE.x, SCREEN_SIZE.y);
        pauseButtonBounds = new Rectangle();
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    protected void inputHandle() {

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
                handler.onRelease(this);
            }
            else {
                playerBox.notMove();
                handler.notTouched();
            }
        }
    }

    @Override
    public void update(float dt) {

        cameraPosition = camera.position;
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        pauseButtonBounds.set((cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x, cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y,
                SHIFT_BEETVEN_PAUSE_RECTS + PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);
        inputHandle();

        switch (gameState){
            case RUNNING:
                playerBox.update(dt);
                camera.position.y = playerBox.getPosition().y + SCREEN_SIZE.y / 2 - START_POSITION.y;
                camera.update();

                int randInt = rand.nextInt(30);
                if (randInt == 1){
                    particles.add(new Particle(new Vector2(cameraPosition.x, cameraPosition.y), SCREEN_SIZE));
                }

                for (Wall wall : walls) {
                    if (cameraPosition.y - SCREEN_SIZE.y / 2 - wall.getThickness() > wall.getPosition().y) {
                        wall.reposition((int) (cameraPosition.y + SCREEN_SIZE.y / 2));
                    }
                    if (wall.isCollides(playerBox.getBounds())) {
                        gsm.set(new MenuState(gsm));
                    }
                }

                for (Iterator<Particle> it = particles.iterator(); it.hasNext();){
                    Particle particle = it.next();
                    if (cameraPosition.y - SCREEN_SIZE.y / 2 > particle.getPosition().y) {
                        it.remove();
                    }
                    else{
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

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        text.draw(batch, scoreString, new Vector2(25, cameraPosition.y + SCREEN_SIZE.y / 2 - 20));
        batch.end();

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(playerBox.getPosition().x, playerBox.getPosition().y, playerBox.getSize().x, playerBox.getSize().y);                         //player
        shapeRenderer.rect(0, cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SIZE.y - 40,                                                             //header
                SCREEN_SIZE.x, PAUSE_BTN_SIZE.y + 40, HEADER_COLOR, HEADER_COLOR, HEADER_COLOR, HEADER_COLOR);

        for (Particle particle : particles){
            particle.draw(shapeRenderer);
        }

        for (Wall wall : walls) {
            shapeRenderer.rect(wall.getPosition().x, wall.getPosition().y, wall.getSize().x, wall.getSize().y);
        }

        switch (gameState) {
            case RUNNING:
                shapeRenderer.rect((cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x,
                        cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y, PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);              //pause
                shapeRenderer.rect((cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x + SHIFT_BEETVEN_PAUSE_RECTS,
                        cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y, PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);              //pause
                shapeRenderer.end();
                return;
            case PAUSED:
                shapeRenderer.rect(cameraPosition.x - SCREEN_SIZE.x / 2, cameraPosition.y - SCREEN_SIZE.y / 2,                                          //screen darker when paused
                        SCREEN_SIZE.x, SCREEN_SIZE.y, PAUSE_COLOR, PAUSE_COLOR, PAUSE_COLOR, PAUSE_COLOR);
                shapeRenderer.triangle((cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x, cameraPosition.y + SCREEN_SIZE.y / 2 - 20,       //resume
                        (cameraPosition.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x, cameraPosition.y + SCREEN_SIZE.y / 2 - PAUSE_BTN_SIZE.y - 20,
                        (cameraPosition.x) * 2 - 20, cameraPosition.y + SCREEN_SIZE.y / 2 - 45);
                shapeRenderer.end();
                return;
        }
    }

    @Override
    public void dispose() {
        playerBox.dispose();
        shapeRenderer.dispose();
        text.dispose();
        for (Wall wall : walls){
            wall.dispose();
        }
    }
}
