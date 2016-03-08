package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import ru.patrushevoleg.minigame.MyGame;

public class Wall extends Entity {

    private static final int MIN_WIDTH = MyGame.WIDTH / 4;
    private static final int MAX_WIDTH = MyGame.WIDTH / 2;

    private Random rand;
    private Rectangle bounds;

    public Wall(int y){
        rand = new Random();
        position = new Vector2(definePosition(y));
        size = new Vector2(defineSize());
        bounds = new Rectangle(defineBounds());
        bounds.setPosition(position.x, position.y);
    }

    public Vector2 definePosition(int y){
        return position = new Vector2(rand.nextInt(MyGame.WIDTH) - MIN_WIDTH / 2, y);
    }

    public Vector2 defineSize(){
        return size = new Vector2(rand.nextInt(MAX_WIDTH) + MIN_WIDTH, 30);
    }

    private Rectangle defineBounds(){
        return bounds = new Rectangle(position.x, position.y , size.x, size.y);
    }

    public void reposition(int y){
        definePosition(y);
        defineSize();
        defineBounds();
        bounds.setPosition(position.x, position.y);
    }

    public boolean isCollides(Rectangle player){
        return player.overlaps(bounds);
    }

    public Vector2 getPosition(){
        return position;
    }

    public Vector2 getSize(){
        return size;
    }

    public int getThickness(){
        return (int)size.y;
    }

    public void dispose(){

    }
}
