package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.actions.MoveConveyor;
import com.lipsum.game.factory.AbstractFactory;
import com.lipsum.game.factory.factories.ConveyorFactory;

import java.util.ArrayList;
import java.util.List;


public class Conveyor extends Building {
    public static AbstractFactory factory = ConveyorFactory.getInstance();
    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST,
    }
    private Direction direction;
    protected Packet packet;

    //TODO: direction should be enum
    public Conveyor(int x, int y, Direction direction){
        super(x, y);
        setColor(0,1,0,1);
        this.direction = direction;
    }

    private ShapeRenderer renderer = new ShapeRenderer();

    public void setPacketLocation(float progress){
        float x = getX();
        float y = getY();
        if (direction == Direction.EAST || direction == Direction.WEST){
            y += 5;
            if (direction == Direction.EAST){
                x += 50*progress - 20;
            } else {
                x += 50*(1-progress) + 20;
            }
        } else {
            x += 5;
            if (direction == Direction.NORTH){
                y += 50*progress - 20;
            } else {
                y += 50*(1-progress) + 20;
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
        if (direction == Direction.NORTH && northBuilding != null){
            b=northBuilding;
        } else if (direction == Direction.SOUTH && southBuilding != null){
            b=southBuilding;
        } else if (direction == Direction.EAST && eastBuilding != null){
            b=eastBuilding;
        } else if (direction == Direction.WEST && westBuilding != null){
            b=westBuilding;
        }
        if (b != null && b instanceof Conveyor) {
            Conveyor c = (Conveyor) b;
            if (!c.hasPacket() && c.allowsInputFrom().contains(this)) {
                c.addPacket(packet);
                packet = null;
            } else {
                //TODO: add listener for updates
            }
        } else {
            //TODO: add listener for updates
        }
    }

    public void addPacket(Packet p){
        if (p == null){
            throw new IllegalStateException("Conveyor already had a packet");
        } else {
            this.packet = p;
            MoveConveyor moveConveyor = new MoveConveyor(this, 0.5f);
            this.addAction(moveConveyor);
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
        if (direction == Direction.NORTH){
            l.add(southBuilding);
        } else if (direction == Direction.SOUTH){
            l.add(northBuilding);
        } else if (direction == Direction.WEST){
            l.add(eastBuilding);
        } else if (direction == Direction.EAST) {
            l.add(westBuilding);
        } return l;
    }
}
