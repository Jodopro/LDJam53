package com.lipsum.game.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.lipsum.game.entities.Conveyor;

public class MoveConveyor extends Action {
    private float progress = 0;
    private float speed;
    private Conveyor conveyor;

    public MoveConveyor(Conveyor c, float speed){
        this.conveyor = c;
        this.speed = speed;
    }
    @Override
    public boolean act(float delta) {
        progress += delta*speed;
        if (progress >= 1){
            progress = 1;
        }
        conveyor.setPacketLocation(progress);
        return progress == 1;
    }
}
