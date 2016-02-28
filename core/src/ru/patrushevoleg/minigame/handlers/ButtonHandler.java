package ru.patrushevoleg.minigame.handlers;

import ru.patrushevoleg.minigame.states.GameStateManager;
import ru.patrushevoleg.minigame.states.PlayState;

public class ButtonHandler {
    private boolean isTouched;

    public ButtonHandler(){
        isTouched = false;
    }

    public void onClick(){
        isTouched = true;
    }

    public void onRelease(GameStateManager gsm){
        if (isTouched == true) {
            gsm.set(new PlayState(gsm));
        }
        isTouched = false;
    }

    public void onRelease(PlayState playState){
        if (isTouched == true) {
            switch (playState.gameState) {
                case RUNNING:
                    isTouched = false;
                    playState.gameState = PlayState.CurrentState.PAUSED;
                    break;
                case PAUSED:
                    isTouched = false;
                    playState.gameState = PlayState.CurrentState.RUNNING;
                    break;
            }
        }
    }

    public void notTouched(){
        isTouched = false;
    }

}
