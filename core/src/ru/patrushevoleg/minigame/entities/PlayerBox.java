package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.math.Vector2;

public class PlayerBox {

    private static final int FORWARD_SPEED = 150;
    private static final int SPEED = 200;
    private static final Vector2 SIZE = new Vector2(70, 70);
    private static final int ZERO_SPEED_SHIFT = 5;

    private Vector2 position;

    private Vector2 speed;
    private Vector2 size;

    public PlayerBox(int x, int y){
        position = new Vector2(x, y);
        speed = new Vector2(0, 150);
        size = new Vector2(SIZE);
    }

    public void update(float dt){
        position.add(speed.x * dt, speed.y * dt);
        //System.out.println(speed.y);
    }

    public void move(int x){
        int centerX = (int)(position.x + size.x / 2);
        if (x > centerX - ZERO_SPEED_SHIFT && x < centerX + ZERO_SPEED_SHIFT) {
            speed.x = 0;
        }
        else if (x > centerX){
            speed.x = SPEED;
        }
        else if (x < centerX){
            speed.x = -SPEED;
        }
    }

    public void notMove(){
        speed.x = 0;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }
}
