package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Vector;

import ru.patrushevoleg.minigame.MyGame;
import ru.patrushevoleg.minigame.entities.PlayerBox;
import ru.patrushevoleg.minigame.entities.Wall;
import ru.patrushevoleg.minigame.handlers.ButtonHandler;

public class PlayState extends State {

    private static final Color PAUSE_COLOR = new Color(0, 0, 0, 0.5f);
    private static final Vector2 START_POSITION = new Vector2(50, 50);
    private static final Vector2 PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS = new Vector2(70, 70);
    private static final Vector2 PAUSE_BTN_SIZE = new Vector2(20, 50);
    private static final int SHIFT_BEETVEN_PAUSE_RECTS = 30;

    public enum CurrentState { RUNNING, PAUSED }
    private ShapeRenderer shapeRenderer;
    private PlayerBox playerBox;
    private Vector<Wall> walls;
    private ButtonHandler handler;
    private Rectangle pauseButtonBounds;

    public CurrentState gameState = CurrentState.RUNNING;

    public PlayState(GameStateManager gsm){
        super(gsm);
        playerBox = new PlayerBox((int)START_POSITION.x, (int)START_POSITION.y);
        shapeRenderer = new ShapeRenderer();
        handler = new ButtonHandler();

        walls = new Vector<Wall>();
        walls.add(new Wall(MyGame.HEIGHT));
        walls.add(new Wall(MyGame.HEIGHT + MyGame.HEIGHT / 2));

        camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
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

        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        pauseButtonBounds.set((camera.position.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x,
                camera.position.y + MyGame.HEIGHT / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y,
                SHIFT_BEETVEN_PAUSE_RECTS + PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);
        inputHandle();

        switch (gameState){
            case RUNNING:
                playerBox.update(dt);
                camera.position.y = playerBox.getPosition().y + MyGame.HEIGHT / 2 - START_POSITION.y;
                camera.update();

                for (Wall wall : walls) {
                    if (camera.position.y - MyGame.HEIGHT / 2 - wall.getThickness() > wall.getPosition().y) {
                        wall.reposition((int) (camera.position.y + MyGame.HEIGHT / 2));
                    }
                    if (wall.isCollides(playerBox.getBounds())) {
                        gsm.set(new MenuState(gsm));
                    }
                }
            case PAUSED:

        }
    }

    @Override
    public void render(SpriteBatch batch) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(playerBox.getPosition().x, playerBox.getPosition().y, playerBox.getSize().x, playerBox.getSize().y);
        for (Wall wall : walls) {
            shapeRenderer.rect(wall.getPosition().x, wall.getPosition().y, wall.getSize().x, wall.getSize().y);
        }

        switch (gameState) {
            case RUNNING:
                shapeRenderer.rect((camera.position.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x,
                        camera.position.y + MyGame.HEIGHT / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y, PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);
                shapeRenderer.rect((camera.position.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x + SHIFT_BEETVEN_PAUSE_RECTS,
                        camera.position.y + MyGame.HEIGHT / 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.y, PAUSE_BTN_SIZE.x, PAUSE_BTN_SIZE.y);
                shapeRenderer.end();
                return;
            case PAUSED:
                shapeRenderer.rect(camera.position.x - MyGame.WIDTH / 2, camera.position.y - MyGame.HEIGHT / 2,
                        MyGame.WIDTH, MyGame.HEIGHT, PAUSE_COLOR, PAUSE_COLOR, PAUSE_COLOR, PAUSE_COLOR);
                shapeRenderer.triangle((camera.position.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x,
                        camera.position.y + MyGame.HEIGHT / 2 - 20,
                        (camera.position.x) * 2 - PAUSE_BTN_SHIFT_FROM_SCREEN_BORDERS.x,
                        camera.position.y + MyGame.HEIGHT / 2 - PAUSE_BTN_SIZE.y - 20,
                        (camera.position.x) * 2 - 20,
                        camera.position.y + MyGame.HEIGHT / 2 - 45);
                shapeRenderer.end();
                return;
        }
    }

    @Override
    public void dispose() {
        playerBox.dispose();
        for (Wall wall : walls){
            wall.dispose();
        }
    }

    public void setState(CurrentState state){
        gameState = state;
    }
}
