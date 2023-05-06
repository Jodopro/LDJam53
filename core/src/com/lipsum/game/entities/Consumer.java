package com.lipsum.game.entities;

import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.managers.building.catalog.BuildingType;
import com.lipsum.game.states.PlayerState;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.PacketType;

import java.util.ArrayList;

public class Consumer extends Conveyor {
    public Consumer(int x, int y, Direction direction, PacketType type){
        super(x, y, BuildingType.CONSUMER, direction, type);
        setColor(1,0,1,1);
        //TODO: might be confusing: als direction = north, dan is de input kant dus south
    }

    public void setPacketLocation(float progress){
        super.setPacketLocation(progress*0.5f);
    }

    @Override
    public boolean passToNext(){
        EntityFactory.getInstance().removeManagedObject(packet);
        packet = null;
        getNextPacket();
        PlayerState.getInstance().addScore(1);
        PlayerState.getInstance().addMoney(1);
        return true;
    }

    @Override
    public void getNextPacket(){
        super.getNextPacket();
        this.currentAction.setMaxProgress(1);
    }

}
