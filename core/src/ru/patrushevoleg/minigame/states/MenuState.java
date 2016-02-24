package ru.patrushevoleg.minigame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.patrushevoleg.minigame.MyGame;

public class MenuState extends State {

    private Texture background;
    private Texture playButton;

    public MenuState(GameStateManager gsm){
        super(gsm);
        background = new Texture("menu_bg.png");
        playButton = new Texture("play_button.png");
    }

    @Override
    public void inputHandle() {
        if (Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dp) {
        inputHandle();
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(background, 0, 0, MyGame.WIDTH, MyGame.HEIGHT);
        batch.draw(playButton, (MyGame.WIDTH / 2) - playButton.getWidth() / 2, (MyGame.HEIGHT / 2) - playButton.getHeight() / 2);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playButton.dispose();
    }
}
