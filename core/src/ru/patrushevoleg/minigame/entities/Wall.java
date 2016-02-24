package ru.patrushevoleg.minigame.entities;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import ru.patrushevoleg.minigame.MyGame;

public class Wall {

    private static final int MIN_WIDTH = MyGame.WIDTH / 5;
    private static final int MAX_WIDTH = MyGame.WIDTH / 2;

    private Vector2 size;
    private Vector2 position;
    private Random rand;

    public Wall(int y){
        rand = new Random();
        position = new Vector2(rand.nextInt(MyGame.WIDTH), y);
        size = new Vector2(rand.nextInt(MAX_WIDTH) + MIN_WIDTH, 20);
    }
}
