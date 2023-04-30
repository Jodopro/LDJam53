package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.actions.CreatePacket;
import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.PacketType;

import java.util.ArrayList;

public class Consumer extends Conveyor {
    public Consumer(int x, int y, Direction direction, PacketType type){
        super(x, y, direction, type);
//        switch(type){
//            case RED -> setColor(1,0,0,1);
//            case BLUE -> setColor(0,0,1,1);
//            case YELLOW -> setColor(1,1,0,1);
//        }
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
        return true;
    }
}
