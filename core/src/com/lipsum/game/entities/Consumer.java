package com.lipsum.game.entities;

import com.lipsum.game.actions.CreatePacket;
import com.lipsum.game.factory.factories.EntityFactory;

import java.util.ArrayList;

public class Consumer extends Conveyor {
    public Consumer(int x, int y, Direction direction){
        super(x, y, direction);
        setColor(1,1,0,1);
        //TODO: might be confusing: als direction = north, dan is de input kant dus south
        getNextPacket();
    }

    public void setPacketLocation(float progress){
        super.setPacketLocation(progress*0.5f);
    }

    @Override
    public boolean passToNext(){
        EntityFactory.getInstance().removeManagedObject(packet);
        packet = null;
        return true;
    }
}
