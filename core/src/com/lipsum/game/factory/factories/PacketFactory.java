package com.lipsum.game.factory.factories;

import com.lipsum.game.entities.Building;
import com.lipsum.game.entities.Packet;
import com.lipsum.game.factory.AbstractFactory;

public class PacketFactory extends AbstractFactory<Packet> {
    private static PacketFactory instance = null;

    static {
        EntityFactory.getInstance().addSubFactory(PacketFactory.getInstance());
    }

    public PacketFactory() {
        super(Packet.class);
    }

    public static PacketFactory getInstance() {
        if (instance == null) {
            instance = new PacketFactory();
        }
        return instance;
    }
}