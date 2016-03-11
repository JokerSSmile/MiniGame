package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class PlayerBox extends Entity{

    private static final float FORWARD_SPEED = 600f;
    private static final Vector2 SIZE = new Vector2(70, 70);
    private static final int ZERO_SPEED_SHIFT = 10;

    private Rectangle bounds;
    private float acceleration;
    private Random rand;
    private Vector2 SCREEN_SIZE;
    private float timer;

    private Color color = new Color(1, 1, 1, 1);

    public PlayerBox(Vector2 startPosition, Vector2 SCREEN_SIZE){
        position = new Vector2(startPosition.x, startPosition.y);
        speed = new Vector2(0, FORWARD_SPEED);
        size = new Vector2(SIZE);
        bounds = new Rectangle(startPosition.x, startPosition.y, SIZE.x, SIZE.y);
        acceleration = 0;
        rand = new Random();
        this.SCREEN_SIZE = SCREEN_SIZE;
    }

    public void update(float dt){
        acceleration += dt;
        speed.y += acceleration / 1000;
        position.add(speed.x * dt, speed.y * dt);
        bounds.setPosition(position.x, position.y);

        timer += dt;
        if (timer >= 0.44){
            color = new Color(rand.nextFloat() * 1 + 0.2f,  rand.nextFloat() * 1 + 0.2f, rand.nextFloat() * 1 + 0.2f, 1);
            timer = 0;
        }
    }

    public void render(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(position.x + (SIZE.x - size.x) / 2, position.y + (SIZE.y - size.y) / 2, size.x, size.y,
                color, color, color, color);
    }

    public void move(Vector3 mousePos){
        float centerX = position.x + size.x / 2;
        if (mousePos.x > centerX - ZERO_SPEED_SHIFT && mousePos.x < centerX + ZERO_SPEED_SHIFT) {
            speed.x = 0;
        }
        else if (mousePos.x > centerX){
            speed.x = (Math.abs(mousePos.x - centerX) * 10);

        }
        else if (mousePos.x < centerX){
            speed.x = -(Math.abs(mousePos.x - centerX) * 10);
        }

        if (centerX < 0) {
            position.x = 0 - size.x / 2;
        }
        else if  (centerX > SCREEN_SIZE.x){
            position.x = SCREEN_SIZE.x - size.x / 2;
        }
    }

    public void notMove(){
        speed.x = 0;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Color getColor(){
        return color;
    }

    public void dispose(){

    }
}
