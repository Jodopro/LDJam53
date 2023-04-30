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
import com.lipsum.game.util.PacketType;
import com.lipsum.game.world.tile.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.lipsum.game.LDJam53.stateTime;
import static com.lipsum.game.util.DrawUtil.drawRotated;


public class Conveyor extends Building {
    private static final Random rand = new Random();
    public static AbstractFactory factory = ConveyorFactory.getInstance();
    private Direction direction = Direction.EAST;

    protected List<PacketType> types;

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

    public Conveyor(int x, int y, BuildingType buildingType, Direction direction) {
        this(x, y, buildingType, direction, List.of(PacketType.values()));
    }

    public Conveyor(int x, int y, BuildingType buildingType, Direction direction, PacketType packetType) {
        this(x, y, buildingType, direction, List.of(packetType));
    }

    public Conveyor(int x, int y, BuildingType buildingType, Direction direction, List<PacketType> packetType) {
        super(x, y);
        this.types = packetType;
        this.buildingType = buildingType;
        this.direction = direction;
        switch (buildingType) {
            case BELT_STRAIGHT, CONSUMER, PRODUCER -> initIO(List.of(direction.opposite()), List.of(direction));
            case BELT_LEFT -> initIO(List.of(direction.rotateLeft()), List.of(direction));
            case BELT_RIGHT -> initIO(List.of(direction.rotateRight()), List.of(direction));
            case MERGER -> initIO(List.of(direction.rotateLeft(), direction.opposite(), direction.rotateRight()),
                    List.of(direction));
            case SPLITTER -> initIO(List.of(direction.opposite()),
                    List.of(direction, direction.rotateLeft(), direction.rotateRight()));
            default -> throw new IllegalArgumentException("Unknown building type " + buildingType);

        }
    }

    private void initIO(List<Direction> inputs, List<Direction> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public void setPacketLocation(float progress) {
        float x = getX() + Tile.WIDTH/2;
        float y = getY()+ Tile.HEIGHT/2;
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
            packet.setPosition(x, y);
        }
    }

    public boolean passToNext() {
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
                return true;
            }
        }
        return false;
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
                currentTo = outputs.get(rand.nextInt(outputs.size()));
                MoveConveyor moveConveyor = new MoveConveyor(this, 2);
                this.addAction(moveConveyor);
                next.previousConveyor.packet = null;
                next.previousConveyor.getNextPacket();
            }
        }
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Texture texture = null;
        if (buildingType != null) {
            texture = switch (buildingType) {
                case BELT_STRAIGHT -> TextureStore.CONVEYOR_BELT_STRAIGHT.getKeyFrame(stateTime, true);
                case BELT_RIGHT -> TextureStore.CONVEYOR_BELT_RIGHT.getKeyFrame(stateTime, true);
                case BELT_LEFT -> TextureStore.CONVEYOR_BELT_LEFT.getKeyFrame(stateTime, true);
                case MERGER -> TextureStore.MERGER.getKeyFrame(stateTime, true);
                case SPLITTER -> TextureStore.SPLITTER.getKeyFrame(stateTime, true);
                default -> null;
            };
        }

        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        if (texture == null) {
            renderer.setColor(getColor());
            renderer.rect(0, 0, getWidth(), getHeight());
        }
        int i = 0;
        for (PacketType type : types) {
            switch (type) {
                case RED -> renderer.setColor(1, 0, 0, 1);
                case BLUE -> renderer.setColor(0, 0, 1, 1);
                case YELLOW -> renderer.setColor(1, 1, 0, 1);
            }
            renderer.rect(i * getWidth() / types.size(), 0, getWidth() / types.size(), 5);
            renderer.rect(getWidth() - 5, i * getHeight() / types.size(), 5, getHeight() / types.size());
            renderer.rect(getWidth() - (1 + i) * getWidth() / types.size(), getHeight() - 5, getWidth() / types.size(), 5);
            renderer.rect(0, getHeight() - (1 + i) * getHeight() / types.size(), 5, getHeight() / types.size());
            i += 1;
        }
        renderer.end();

        batch.begin();
        if (texture != null) {
            drawRotated(batch, texture, getX(), getY(),
                    (float) (direction.rotateRight().toRadians() * (180.0f / Math.PI)), getWidth(), getHeight());
        }
    }
}
