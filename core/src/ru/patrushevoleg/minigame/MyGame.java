package ru.patrushevoleg.minigame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.patrushevoleg.minigame.states.GameStateManager;
import ru.patrushevoleg.minigame.states.MenuState;

public class MyGame extends ApplicationAdapter {

	public static final int WIDTH = 486;
	public static final int HEIGHT = 864;

	public static final String TITLE = "my game";

	private GameStateManager gsm;
	private SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.input.setCatchBackKey(true);

		gsm.push(new MenuState(gsm));
	}

	@Override
	public void pause() {
		gsm.pause();
		super.pause();
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
		gsm.dispose();
	}
}
