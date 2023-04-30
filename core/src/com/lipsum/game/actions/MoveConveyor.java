package com.lipsum.game.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.lipsum.game.entities.Conveyor;

public class MoveConveyor extends Action {
    private float progress = 0;
    private float maxProgress;
    private float speed;
    private Conveyor conveyor;

    public MoveConveyor(Conveyor c, float speed, float maxProgress){
        this.conveyor = c;
        this.speed = speed;
        this.maxProgress = maxProgress;
    }
    @Override
    public boolean act(float delta) {
        progress += delta*speed;
        if (progress >= maxProgress){
            progress = maxProgress;
        }
        conveyor.setPacketLocation(progress);
        if (progress == 1){
            return conveyor.passToNext();
        }
        return false;
    }

    public void capProgress(float progress) {
        if (this.progress >= progress){
            this.progress = progress;
        }
    }

    public void setMaxProgress(float maxProgress){
        this.maxProgress = maxProgress;
    }
}
