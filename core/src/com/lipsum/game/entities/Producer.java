package com.lipsum.game.entities;

import com.lipsum.game.actions.CreatePacket;

import java.util.ArrayList;

public class Producer extends Conveyor {
    public Producer(int x, int y, Conveyor.Direction direction){
        super(x, y, direction);
        setColor(0,1,1,1);
        inputs = new ArrayList<>();
        currentTo = direction;
        getNextPacket();
    }

    public void setPacketLocation(float progress){
        super.setPacketLocation(progress*0.5f + 0.5f);
    }


    @Override
    protected void getNextPacket() {
        if (packet == null) {
            packet = new Packet();
            CreatePacket createPacket = new CreatePacket(this, 1);
            this.addAction(createPacket);
        }
    }
}
