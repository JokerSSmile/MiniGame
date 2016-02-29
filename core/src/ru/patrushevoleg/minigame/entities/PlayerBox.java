package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PlayerBox {

    private static final float FORWARD_SPEED = 600f;
    private static final float SIDE_SPEED = 500f;
    private static final Vector2 SIZE = new Vector2(70, 70);
    private static final int ZERO_SPEED_SHIFT = 7;

    private Vector2 position;
    private Vector2 speed;
    private Vector2 size;
    private Rectangle bounds;
    private float acceleration;

    public PlayerBox(Vector2 startPosition){
        position = new Vector2(startPosition.x, startPosition.y);
        speed = new Vector2(0, FORWARD_SPEED);
        size = new Vector2(SIZE);
        bounds = new Rectangle(startPosition.x, startPosition.y, SIZE.x, SIZE.y);
        acceleration = 0;
    }

    public void update(float dt){
        acceleration += dt;
        speed.y += acceleration / 1000;
        position.add(speed.x * dt, speed.y * dt);

        bounds.setPosition(position.x, position.y);
    }

    public void move(Vector3 mousePos){
        float centerX = position.x + size.x / 2;
        if (mousePos.x > centerX - ZERO_SPEED_SHIFT && mousePos.x < centerX + ZERO_SPEED_SHIFT) {
            speed.x = 0;
        }
        else if (mousePos.x > centerX){
            speed.x = SIDE_SPEED + (Math.abs(mousePos.x - centerX) * 3);
        }
        else if (mousePos.x < centerX){
            speed.x = -SIDE_SPEED - (Math.abs(mousePos.x - centerX) * 3);
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

    public Vector2 getSize() {
        return size;
    }

    public void dispose(){

    }
}
