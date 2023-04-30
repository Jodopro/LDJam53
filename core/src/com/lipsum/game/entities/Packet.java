package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.LDJam53;
import com.lipsum.game.TextureStore;
import com.lipsum.game.factory.factories.ConveyorFactory;
import com.lipsum.game.factory.factories.PacketFactory;
import com.lipsum.game.util.PacketType;
import com.lipsum.game.world.tile.Tile;

import java.util.EnumMap;

public class Packet extends Entity {
    private static final EnumMap<PacketType, TextureRegion> textureRegions;
    static {
        // This code assumes that the texture for each packet is square (which it currently is)
        // That way, we can use the height as the width of a packet in the map
        textureRegions = new EnumMap<>(PacketType.class);
        textureRegions.put(PacketType.RED, new TextureRegion(TextureStore.PACKET,
                TextureStore.PACKET.getHeight()*2, 0, TextureStore.PACKET.getHeight(), TextureStore.PACKET.getHeight()));
        textureRegions.put(PacketType.BLUE, new TextureRegion(TextureStore.PACKET,
                0, 0, TextureStore.PACKET.getHeight(), TextureStore.PACKET.getHeight()));
        textureRegions.put(PacketType.YELLOW, new TextureRegion(TextureStore.PACKET,
                TextureStore.PACKET.getHeight(), 0, TextureStore.PACKET.getHeight(), TextureStore.PACKET.getHeight()));
    }
    private PacketType type;



    public Packet(PacketType type){
        super();
        setWidth(textureRegions.get(PacketType.RED).getRegionWidth()* RENDER_SCALE);
        setHeight(textureRegions.get(PacketType.RED).getRegionHeight()* RENDER_SCALE);
        this.type = type;
        LDJam53.packetGroup.addActor(this);
    }

    public void draw (Batch batch, float parentAlpha) {
        batch.draw(textureRegions.get(type), getX() - getWidth()/2, getY() - getHeight()/2,  getWidth(), getHeight());

    }

    @Override
    public void onDispose() {
        System.out.println("dispose Packet");
        remove();
    }

    public PacketType getType() {
        return type;
    }
}
