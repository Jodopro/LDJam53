package com.lipsum.game.entities;

import com.lipsum.game.actions.CreatePacket;
import com.lipsum.game.managers.building.catalog.BuildingType;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.PacketType;

import java.util.ArrayList;
import java.util.Random;

public class Producer extends Conveyor {
    private static final Random rand = new Random();
    public Producer(int x, int y, Direction direction, PacketType type){
        super(x, y, BuildingType.PRODUCER, direction, type);
        setColor(0,1,1,1);
        inputs = new ArrayList<>();
        currentTo = direction;
        currentFrom = direction.opposite();
        getNextPacket();
    }

    public void setPacketLocation(float progress){
        super.setPacketLocation(progress*0.5f + 0.5f);
    }


    @Override
    protected void getNextPacket() {
        if (packet == null) {
            packet = new Packet(types.get(rand.nextInt(types.size())));
            CreatePacket createPacket = new CreatePacket(this, 0.5f);
            this.addAction(createPacket);
        }
    }
}
