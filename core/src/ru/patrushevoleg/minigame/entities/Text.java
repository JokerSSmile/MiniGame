package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Text {

    private BitmapFont font;
    private Vector2 size;
    private GlyphLayout layout;
    private Rectangle bounds;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public Text(FreeTypeFontGenerator generator, int fontSize, boolean isWhite){
        font = new BitmapFont();
        layout = new GlyphLayout();

        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        if (!isWhite){
            parameter.color = new Color(0, 0, 0, 1);
        }
        font = generator.generateFont(parameter);
        bounds = new Rectangle();
    }

    public void draw(SpriteBatch batch, String string, Vector2 position){
        font.draw(batch, string, position.x, position.y);
        layout.setText(font, string);
        size = new Vector2(layout.width, layout.height);
        bounds.setSize(size.x, size.y);
        bounds.setPosition(position.x, position.y - size.x / 2);
    }

    public void draw(SpriteBatch batch, int number, Vector2 position){
        font.draw(batch, "" + number, position.x, position.y);
        layout.setText(font, "" + number);
        size = new Vector2(layout.width, layout.height);
        bounds.setSize(size.x, size.y);
        bounds.setPosition(position.x, position.y - size.x / 2);
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void dispose(){

    }
}
