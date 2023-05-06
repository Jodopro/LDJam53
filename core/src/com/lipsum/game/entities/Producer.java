package com.lipsum.game.entities;

import com.lipsum.game.actions.CreatePacket;
import com.lipsum.game.managers.building.catalog.BuildingType;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.PacketType;

import java.util.ArrayList;
import java.util.List;
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
            currentTo = direction;
            CreatePacket createPacket = new CreatePacket(this, 0.5f);
            currentAction = createPacket;
            this.addAction(createPacket);
        }
    }

    @Override
    public void onUpdateNeighbour(Direction direction){
        if (packet != null && (currentTo == direction || currentTo == null)){
            List<Direction> validOutputs = getValidOutputDirections(packet.getType());
            if (!validOutputs.contains(currentTo)){
                currentAction.capProgress(0.5f);
                if (validOutputs.size() >= 1){
                    currentTo = validOutputs.get(rand.nextInt(validOutputs.size()));
                    currentAction.setMaxProgress(1);
                } else {
                    currentTo = null;
                    currentAction.setMaxProgress(0.5f);
                }
            }
            if (currentAction != null && !this.getActions().contains(currentAction, true)){
                this.addAction(currentAction);
            }
        }
    }
}
