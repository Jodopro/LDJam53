package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.actions.MoveConveyor;
import com.lipsum.game.factory.AbstractFactory;
import com.lipsum.game.factory.factories.ConveyorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Conveyor extends Building {
    public static AbstractFactory factory = ConveyorFactory.getInstance();
    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
    }
    private class Waiting{
        Conveyor.Direction direction;
        Packet packet;
        Conveyor previousConveyor;
        private Waiting(Conveyor.Direction d, Packet p, Conveyor c){
            direction = d;
            packet = p;
            this.previousConveyor = c;
        }
    }
    private List<Direction> inputs;
    private List<Direction> outputs;
    private Direction currentFrom;
    private Direction currentTo;
    protected Packet packet;
    private Queue<Waiting> waitingQueue = new ConcurrentLinkedQueue();

    public Conveyor(int x, int y, Direction direction){
        super(x, y);
        setColor(0,1,0,1);
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
        outputs.add(direction);
        switch (direction) {
            case EAST -> inputs.add(Direction.WEST);
            case WEST -> inputs.add(Direction.EAST);
            case SOUTH -> inputs.add(Direction.NORTH);
            case NORTH -> inputs.add(Direction.SOUTH);
        }
    }

    public Conveyor(int x, int y, List<Direction> inputs, List<Direction> outputs){
        super(x, y);
        setColor(0,1,0,1);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    private ShapeRenderer renderer = new ShapeRenderer();

    public void setPacketLocation(float progress){
        float x = getX() + 5;
        float y = getY() + 5;
        if (progress < 0.5f){
            switch (currentFrom){
                case SOUTH -> y += 50*progress - 25;
                case WEST -> x += 50*progress - 25;
                case EAST -> x += 50*(1-progress) - 25;
                case NORTH -> y += 50*(1-progress) - 25;
            }
        } else {
            switch (currentTo){
                case WEST -> x += 50*(1-progress) - 25;
                case SOUTH -> y += 50*(1-progress) - 25;
                case NORTH -> y += 50*progress - 25;
                case EAST -> x += 50*progress - 25;
            }
        }
        if (packet == null){
            throw new IllegalStateException("Conveyor has not packet");
        } else {
            packet.setBounds(x, y, 40, 40);
        }
    }

    public void passToNext(){
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
        System.out.println(d);
        if (b != null && b instanceof Conveyor) {
            Conveyor c = (Conveyor) b;
            if (c.allowsInputFrom().contains(this)) {
                c.addPacket(packet, this, d);
            } else {
                //TODO: add listener for updates
            }
        } else {
            //TODO: add listener for updates
        }
        if (packet == null){
            getNextPacket();
        }

    }

    public void addPacket(Packet p, Conveyor from, Direction d){
        if (p == null){
            throw new IllegalStateException("Conveyor already had a packet");
        } else {
            Waiting w = new Waiting(d, p, from);
            waitingQueue.add(w);
            getNextPacket();
        }
    }

    private void getNextPacket(){
        Waiting next = waitingQueue.poll();
        if (next != null){
            packet = next.packet;
            currentFrom = next.direction;
            Random rand = new Random();
            currentTo = outputs.get(rand.nextInt(outputs.size()));
            MoveConveyor moveConveyor = new MoveConveyor(this, 1);
            this.addAction(moveConveyor);
            next.previousConveyor.packet = null;
        }
    }

    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(getColor());
        renderer.rect(0, 0, getWidth(), getHeight());
        renderer.end();

        batch.begin();
    }

    public boolean hasPacket(){
        return packet != null;
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

    @Deprecated
    public void setCurrentFrom(Direction currentFrom) {
        this.currentFrom = currentFrom;
    }

    @Deprecated
    public void setCurrentTo(Direction currentTo) {
        this.currentTo = currentTo;
    }

    @Deprecated
    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
