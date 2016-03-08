package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Particle extends Entity {

    private static final int SIZE = 5;

    private Random rand;
    private Color color;

    public Particle(Vector2 camPosition, Vector2 screenSize, Color color){
        rand = new Random();
        position = new Vector2(rand.nextInt((int)screenSize.x), camPosition.y + screenSize.y);
        speed = new Vector2(rand.nextInt(600) + 150, 0);
        this.color = color;
    }

    public void update(float dt){
        position.add(0, -speed.x * dt);
    }

    public void draw(ShapeRenderer renderer){
        renderer.setColor(color);
        renderer.rect(position.x, position.y, SIZE, SIZE);
        //renderer.setColor(1, 1, 1, 1);
    }

    public Vector2 getPosition(){
        return position;
    }

    public void dispose(){

    }
}
