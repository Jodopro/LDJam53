package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.factory.factories.ConveyorFactory;


public class Conveyor extends Building {
    private Packet packet;
    private String direction;

    //TODO: direction should be enum
    public Conveyor(int x, int y, String direction){
        super();
        setBounds(x, y, 50, 50);
        setColor(0,1,0,1);
        this.direction = direction;

        ConveyorFactory.getInstance().addManagedObject(this);
    }

    private ShapeRenderer renderer = new ShapeRenderer();

    public void addPacket(Packet p){
        if (p == null){
            throw new IllegalStateException("Conveyor already had a packet");
        } else {
            this.packet = p;
        }
    }

    public void setPacketLocation(float progress){
        float x = getX();
        float y = getY();
        if (direction == "east" || direction == "west"){
            y += 5;
            if (direction == "east"){
                x += 50*progress;
            } else {
                x += 50*(1-progress);
            }
        } else {
            x += 5;
            if (direction == "north"){
                y += 50*progress;
            } else {
                y += 50*(1-progress);
            }
        }
        if (packet == null){
            throw new IllegalStateException("Conveyor has not packet");
        } else {
            packet.setBounds(x, y, 40, 40);
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
}
