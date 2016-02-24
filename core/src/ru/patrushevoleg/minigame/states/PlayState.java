package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Vector;

import ru.patrushevoleg.minigame.MyGame;
import ru.patrushevoleg.minigame.entities.PlayerBox;
import ru.patrushevoleg.minigame.entities.Wall;

public class PlayState extends State {

    private ShapeRenderer shapeRenderer;
    private PlayerBox playerBox;
    private Texture background;
    private Vector<Wall> walls;

    public PlayState(GameStateManager gsm){
            super(gsm);
            background = new Texture("menu_bg.png");
            playerBox = new PlayerBox(50, 50);
            shapeRenderer = new ShapeRenderer();
            camera.setToOrtho(false, MyGame.WIDTH, MyGame.HEIGHT);
    }

    @Override
    protected void inputHandle() {
        if (Gdx.input.isTouched())
        {
            playerBox.move(Gdx.input.getX());
        }
        else
        {
            playerBox.notMove();
        }
    }

    @Override
    public void update(float dt) {
        inputHandle();
        playerBox.update(dt);

        camera.position.y = playerBox.getPosition().y;
        camera.update();

        System.out.println()

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, MyGame.WIDTH, MyGame.HEIGHT);
        batch.end();


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(playerBox.getPosition().x, playerBox.getPosition().y, playerBox.getSize().x, playerBox.getSize().y);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {

    }
}
