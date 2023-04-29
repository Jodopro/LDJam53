package com.lipsum.game.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.entities.Producer;

public class CreatePacket extends Action {
    private float progress = 0;
    private float speed;
    private Producer producer;

    public CreatePacket(Producer p, float speed){
        this.producer = p;
        this.speed = speed;
    }
    @Override
    public boolean act(float delta) {
        progress += delta*speed;
        if (progress >= 1){
            progress = 1;
        }
        producer.setPacketLocation(progress);
        if (progress == 1){
            producer.passToNext();
            return true;
        }
        return false;
    }
}
