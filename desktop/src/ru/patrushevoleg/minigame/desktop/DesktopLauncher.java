package ru.patrushevoleg.minigame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.patrushevoleg.minigame.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = MyGame.WIDTH;
		config.height = MyGame.HEIGHT;
		config.title = MyGame.TITLE;

		new LwjglApplication(new MyGame(), config);
	}
}
