package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;

public class Text {
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private BitmapFont font;

    public Text(int fontSize){
        font = new BitmapFont();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("manteka.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        font = generator.generateFont(parameter);
    }

    public void draw(SpriteBatch batch, String string, Vector2 position){
        font.draw(batch, string, position.x, position.y);
    }

    public void dispose(){
        generator.dispose();
    }
}
