package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.LDJam53;
import com.lipsum.game.factory.factories.ConveyorFactory;
import com.lipsum.game.factory.factories.PacketFactory;

public class Packet extends Entity {

    public Packet(){
        super();
        setColor(0,0,1,1);
        LDJam53.packetGroup.addActor(this);
    }

    private ShapeRenderer renderer = new ShapeRenderer();
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

    @Override
    public void onDispose() {
        remove();
    }
}
