package com.lipsum.game.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.lipsum.game.entities.Producer;
import com.lipsum.game.states.PlayerState;

public class WaitingHealtDecrease extends Action {
    private float speed;
    private Producer producer;

    public WaitingHealtDecrease(Producer p, float speed){
        this.producer = p;
        this.speed = speed;
    }
    @Override
    public boolean act(float delta) {
        if(producer.isWaiting()){
            PlayerState.getInstance().subtractHealth(delta*speed);
        }
        return false;
    }
}
