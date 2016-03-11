package ru.patrushevoleg.minigame.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import ru.patrushevoleg.minigame.MyGame;
import ru.patrushevoleg.minigame.states.GameStateManager;
import ru.patrushevoleg.minigame.states.PlayState;
import ru.patrushevoleg.minigame.states.State;
import ru.patrushevoleg.minigame.states.StatisticsState;

public class ButtonHandler {

    private boolean isTouched;

    public ButtonHandler(){
        isTouched = false;
    }

    public void onClick(){
        isTouched = true;
    }

    public void onRelease(GameStateManager gsm, State state){
        if (isTouched) {
            gsm.set(state);
        }
    }

    public void onRelease(){
        if (isTouched) {
            Gdx.app.exit();
        }
        isTouched = false;
    }

    public boolean isOnRelease(){
        return isTouched;
    }

    public void onRelease(PlayState playState, Music music){
        if (isTouched) {
            switch (playState.gameState) {
                case RUNNING:
                    isTouched = false;
                    playState.gameState = PlayState.CurrentState.PAUSED;
                    music.pause();
                    break;
                case PAUSED:
                    isTouched = false;
                    playState.gameState = PlayState.CurrentState.RUNNING;
                    music.play();
                    break;
            }
        }
    }

    public void onRelease(StatisticsState statState){
        if (isTouched) {
            switch (statState.gameState) {
                case NORMAL:
                    isTouched = false;
                    statState.gameState = StatisticsState.CurrentState.CONFIRM_RESET;
                    break;
                case CONFIRM_RESET:
                    isTouched = false;
                    statState.gameState = StatisticsState.CurrentState.NORMAL;
                    break;
            }
        }
    }

    public void notTouched(){
        isTouched = false;
    }
}
