package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.TextureStore;
import com.lipsum.game.actions.MoveConveyor;
import com.lipsum.game.factory.AbstractFactory;
import com.lipsum.game.factory.factories.ConveyorFactory;
import com.lipsum.game.managers.building.catalog.BuildingType;
import com.lipsum.game.util.Direction;
import com.lipsum.game.world.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.lipsum.game.LDJam53.stateTime;
import static com.lipsum.game.util.DrawUtil.drawRotated;


public class Conveyor extends Building {
    public static AbstractFactory factory = ConveyorFactory.getInstance();
    private Direction direction = Direction.EAST;

    private class Waiting {
        Direction direction;
        Packet packet;
        Conveyor previousConveyor;

        private Waiting(Direction d, Packet p, Conveyor c) {
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

    private BuildingType buildingType;

    public Conveyor(int x, int y, Direction direction) {
        this(x, y, BuildingType.BELT_STRAIGHT, direction);
    }

    public Conveyor(int x, int y, BuildingType type, Direction direction) {
        super(x, y);

        buildingType = type;
        this.direction = direction;
        switch (type) {
            case BELT_STRAIGHT -> init(x ,y, List.of(direction.opposite()), List.of(direction));
            case BELT_LEFT -> init(x, y, List.of(direction.rotateLeft()), List.of(direction));
            case BELT_RIGHT -> init(x, y, List.of(direction.rotateRight()), List.of(direction));
            case MERGER -> init(x, y, List.of(direction.rotateLeft(), direction.opposite(), direction.rotateRight()),
                    List.of(direction));
            case SPLITTER -> init(x, y, List.of(direction.opposite()),
                    List.of(direction, direction.rotateLeft(), direction.rotateRight()));
            default -> throw new IllegalArgumentException("Unknown building type " + buildingType);
        }
    }

    private void init(int x, int y, List<Direction> inputs, List<Direction> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    @Deprecated
    public Conveyor(int x, int y, List<Direction> inputs, List<Direction> outputs) {
        super(x, y);
        init(x, y, inputs, outputs);
    }

    private ShapeRenderer renderer = new ShapeRenderer();

    public void setPacketLocation(float progress) {
        float x = getX() + Tile.WIDTH / 10;
        float y = getY() + Tile.HEIGHT / 10;
        if (progress < 0.5f) {
            switch (currentFrom) {
                case SOUTH -> y += Tile.HEIGHT * progress - Tile.HEIGHT / 2;
                case WEST -> x += Tile.WIDTH * progress - Tile.WIDTH / 2;
                case EAST -> x += Tile.WIDTH * (1 - progress) - Tile.WIDTH / 2;
                case NORTH -> y += Tile.HEIGHT * (1 - progress) - Tile.HEIGHT / 2;
            }
        } else {
            switch (currentTo) {
                case WEST -> x += Tile.WIDTH * (1 - progress) - Tile.WIDTH / 2;
                case SOUTH -> y += Tile.HEIGHT * (1 - progress) - Tile.HEIGHT / 2;
                case NORTH -> y += Tile.HEIGHT * progress - Tile.HEIGHT / 2;
                case EAST -> x += Tile.WIDTH * progress - Tile.WIDTH / 2;
            }
        }
        if (packet == null) {
            throw new IllegalStateException("Conveyor has not packet");
        } else {
            packet.setBounds(x, y, Tile.WIDTH * 8 / 10, Tile.HEIGHT * 8 / 10);
        }
    }

    public void passToNext() {
        Building b = null;
        Direction d = null;
        if (currentTo == Direction.NORTH && northBuilding != null) {
            b = northBuilding;
            d = Direction.SOUTH;
        } else if (currentTo == Direction.SOUTH && southBuilding != null) {
            b = southBuilding;
            d = Direction.NORTH;
        } else if (currentTo == Direction.EAST && eastBuilding != null) {
            b = eastBuilding;
            d = Direction.WEST;
        } else if (currentTo == Direction.WEST && westBuilding != null) {
            b = westBuilding;
            d = Direction.EAST;
        }
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
//        if (packet == null){
//            getNextPacket();
//        }

    }

    public void addPacket(Packet p, Conveyor from, Direction d) {
        Waiting w = new Waiting(d, p, from);
        waitingQueue.add(w);
        if (packet == null) {
            getNextPacket();
        }
    }

    protected void getNextPacket() {
        if (packet == null) {
            Waiting next = waitingQueue.poll();
            if (next != null) {
                packet = next.packet;
                currentFrom = next.direction;
                Random rand = new Random();
                currentTo = outputs.get(rand.nextInt(outputs.size()));
                MoveConveyor moveConveyor = new MoveConveyor(this, 1);
                this.addAction(moveConveyor);
                next.previousConveyor.packet = null;
                next.previousConveyor.getNextPacket();
            }
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        Texture texture = null;
        if (buildingType != null) {
            texture = switch (buildingType) {
                case BELT_STRAIGHT -> TextureStore.CONVEYOR_BELT_STRAIGHT.getKeyFrame(stateTime, true);
                case BELT_RIGHT -> TextureStore.CONVEYOR_BELT_RIGHT.getKeyFrame(stateTime, true);
                case BELT_LEFT -> TextureStore.CONVEYOR_BELT_LEFT.getKeyFrame(stateTime, true);
                default -> null;
            };
        }

        if (texture == null) {
            batch.end();

            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.setTransformMatrix(batch.getTransformMatrix());
            renderer.translate(getX(), getY(), 0);

            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(getColor());
            renderer.rect(0, 0, getWidth(), getHeight());
            renderer.end();

            batch.begin();
        } else {
            drawRotated(batch, texture, getX(), getY(),
                    (float) (direction.rotateRight().toRadians() * (180.0f / Math.PI)), getWidth(), getHeight());
        }
    }

    public boolean hasPacket() {
        return packet != null;
    }

    public List<Building> allowsInputFrom() {
        List<Building> l = new ArrayList<>();
        for (Direction d : inputs) {
            switch (d) {
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
