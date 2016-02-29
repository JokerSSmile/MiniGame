package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ru.patrushevoleg.minigame.MyGame;
import ru.patrushevoleg.minigame.handlers.ButtonHandler;

public class EndGame extends State {

    private static final Vector2 screenCenter = new Vector2(MyGame.WIDTH / 2, MyGame.HEIGHT / 2);
    //private static final Rectangle playButtonBounds = new Rectangle(screenCenter.x - 40, screenCenter.y - 50, 90, 100);

    private ShapeRenderer shapeRenderer;
    private ButtonHandler handler;

    public EndGame(GameStateManager gsm){
        super(gsm);
        shapeRenderer = new ShapeRenderer();
        handler = new ButtonHandler();

        camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void inputHandle() {
        mousePosition = viewport.unproject(mousePosition);

    }

    @Override
    public void update(float dp) {
        mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        inputHandle();
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.triangle(screenCenter.x + 50, screenCenter.y, screenCenter.x - 40, screenCenter.y + 50, screenCenter.x - 40, screenCenter.y - 50);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
