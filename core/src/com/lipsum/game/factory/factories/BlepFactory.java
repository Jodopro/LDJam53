package com.lipsum.game.factory.factories;

import com.lipsum.game.entity.entities.Blep;
import com.lipsum.game.factory.AbstractFactory;

public class BlepFactory extends AbstractFactory<Blep> {
    private static BlepFactory instance = null;

    static {
        MovingEntityFactory.getInstance().addSubFactory(BlepFactory.getInstance());
    }

    public BlepFactory() {
        super(Blep.class);
    }

    public static BlepFactory getInstance() {
        if (instance == null) {
            instance = new BlepFactory();
        }
        return instance;
    }

}
