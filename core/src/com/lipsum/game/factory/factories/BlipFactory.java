package com.lipsum.game.factory.factories;

import com.lipsum.game.entity.entities.Blip;
import com.lipsum.game.factory.AbstractFactory;

public class BlipFactory extends AbstractFactory<Blip> {
    private static BlipFactory instance = null;

    static {
        MovingEntityFactory.getInstance().addSubFactory(BlipFactory.getInstance());
    }

    public BlipFactory() {
        super(Blip.class);
    }

    public static BlipFactory getInstance() {
        if (instance == null) {
            instance = new BlipFactory();
        }
        return instance;
    }

}
