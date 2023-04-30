package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.actions.MoveConveyor;
import com.lipsum.game.factory.AbstractFactory;
import com.lipsum.game.factory.factories.ConveyorFactory;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.PacketType;
import com.lipsum.game.world.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Conveyor extends Building {
    private static final Random rand = new Random();
    public static AbstractFactory factory = ConveyorFactory.getInstance();
    protected List<PacketType> types;
    private class Waiting{
        Direction direction;
        Packet packet;
        Conveyor previousConveyor;
        private Waiting(Direction d, Packet p, Conveyor c){
            direction = d;
            packet = p;
            this.previousConveyor = c;
        }
    }
    protected List<Direction> inputs;
    protected List<Direction> outputs;
    protected Direction currentFrom;
    protected Direction currentTo;
    protected Packet packet;
    private Queue<Waiting> waitingQueue = new ConcurrentLinkedQueue();

    public Conveyor(int x, int y, Direction direction){
        this(x, y, new ArrayList<>(), new ArrayList<>());
        outputs.add(direction);
        switch (direction) {
            case EAST -> inputs.add(Direction.WEST);
            case WEST -> inputs.add(Direction.EAST);
            case SOUTH -> inputs.add(Direction.NORTH);
            case NORTH -> inputs.add(Direction.SOUTH);
        }
    }

    public Conveyor(int x, int y, Direction direction, PacketType type){
        this(x, y, new ArrayList<>(), new ArrayList<>(), type);
        outputs.add(direction);
        switch (direction) {
            case EAST -> inputs.add(Direction.WEST);
            case WEST -> inputs.add(Direction.EAST);
            case SOUTH -> inputs.add(Direction.NORTH);
            case NORTH -> inputs.add(Direction.SOUTH);
        }
    }

    public Conveyor(int x, int y, List<Direction> inputs, List<Direction> outputs, List<PacketType> types){
        super(x, y);
        this.types = types;
        setColor(0,1,0,1);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public Conveyor(int x, int y, List<Direction> inputs, List<Direction> outputs){
        this(x, y, inputs, outputs, new ArrayList<>());
        types.add(PacketType.RED);
        types.add(PacketType.YELLOW);
        types.add(PacketType.BLUE);
    }

    public Conveyor(int x, int y, List<Direction> inputs, List<Direction> outputs, PacketType type){
        this(x, y, inputs, outputs, new ArrayList<>());
        types.add(type);
    }

    public void setPacketLocation(float progress){
        float x = getX() + Tile.WIDTH/10;
        float y = getY() + Tile.HEIGHT/10;
        if (progress < 0.5f){
            switch (currentFrom){
                case SOUTH -> y += Tile.HEIGHT*progress - Tile.HEIGHT/2;
                case WEST -> x += Tile.WIDTH*progress - Tile.WIDTH/2;
                case EAST -> x += Tile.WIDTH*(1-progress) - Tile.WIDTH/2;
                case NORTH -> y += Tile.HEIGHT*(1-progress) - Tile.HEIGHT/2;
            }
        } else {
            switch (currentTo){
                case WEST -> x += Tile.WIDTH*(1-progress) - Tile.WIDTH/2;
                case SOUTH -> y += Tile.HEIGHT*(1-progress) - Tile.HEIGHT/2;
                case NORTH -> y += Tile.HEIGHT*progress - Tile.HEIGHT/2;
                case EAST -> x += Tile.WIDTH*progress - Tile.WIDTH/2;
            }
        }
        if (packet == null){
            throw new IllegalStateException("Conveyor has not packet");
        } else {
            packet.setBounds(x, y, Tile.WIDTH*8/10, Tile.HEIGHT*8/10);
        }
    }

    public boolean passToNext(){
        Building b = null;
        Direction d = null;
        if (currentTo == Direction.NORTH && northBuilding != null){
            b=northBuilding;
            d=Direction.SOUTH;
        } else if (currentTo == Direction.SOUTH && southBuilding != null){
            b=southBuilding;
            d=Direction.NORTH;
        } else if (currentTo == Direction.EAST && eastBuilding != null){
            b=eastBuilding;
            d=Direction.WEST;
        } else if (currentTo == Direction.WEST && westBuilding != null){
            b=westBuilding;
            d=Direction.EAST;
        }
        if (b != null && b instanceof Conveyor) {
            Conveyor c = (Conveyor) b;
            if (c.allowsInputFrom().contains(this)) {
                c.addPacket(packet, this, d);
                return true;
            }
        }
        return false;
    }

    public void addPacket(Packet p, Conveyor from, Direction d){
        Waiting w = new Waiting(d, p, from);
        waitingQueue.add(w);
        if (packet == null){
            getNextPacket();
        }
    }

    protected void getNextPacket(){
        if (packet == null){
            Waiting next = waitingQueue.poll();
            if (next != null){
                packet = next.packet;
                currentFrom = next.direction;
                currentTo = outputs.get(rand.nextInt(outputs.size()));
                MoveConveyor moveConveyor = new MoveConveyor(this, 2);
                this.addAction(moveConveyor);
                next.previousConveyor.packet = null;
                next.previousConveyor.getNextPacket();
            }
        }
    }


    public List<Building> allowsInputFrom(){
        List<Building> l = new ArrayList<>();
        for (Direction d:inputs){
            switch (d){
                case EAST -> l.add(eastBuilding);
                case WEST -> l.add(westBuilding);
                case NORTH -> l.add(northBuilding);
                case SOUTH -> l.add(southBuilding);
            }
        }
        return l;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(getColor());
        renderer.rect(0, 0, getWidth(), getHeight());
        int i = 0;
        for (PacketType type:types){
            switch(type){
                case RED -> renderer.setColor(1,0,0,1);
                case BLUE -> renderer.setColor(0,0,1,1);
                case YELLOW -> renderer.setColor(1,1,0,1);
            }
            renderer.rect(i*getWidth()/types.size(), 0, getWidth()/types.size(), 5);
            renderer.rect(getWidth()-5, i*getHeight()/types.size(), 5, getHeight()/types.size());
            renderer.rect(getWidth() - (1+i)*getWidth()/types.size(), getHeight()-5, getWidth()/types.size(), 5);
            renderer.rect(0, getHeight() - (1+i)*getHeight()/types.size(), 5, getHeight()/types.size());
            i += 1;
        }
        renderer.end();

        batch.begin();
    }
}
