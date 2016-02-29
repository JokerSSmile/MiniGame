package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Particle{

    private static final int SIZE = 5;

    private Random rand;
    private Vector2 position;
    private int speed;

    public Particle(Vector2 camPosition, Vector2 screenSize){
        rand = new Random();
        position = new Vector2(rand.nextInt((int)screenSize.x), camPosition.y + screenSize.y);
        speed = rand.nextInt(600) + 150;
    }

    public void update(float dt){
        position.add(0, -speed * dt);
    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(0.6f, 0.6f, 0.6f, 1);
        renderer.rect(position.x, position.y, SIZE, SIZE);
        renderer.setColor(1, 1, 1, 1);
    }

    public Vector2 getPosition(){
        return position;
    }

    public void dispose(){

    }
}
