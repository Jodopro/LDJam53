package com.lipsum.game.factory.factories;

import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.entities.Producer;
import com.lipsum.game.factory.AbstractFactory;

public class ProducerFactory extends AbstractFactory<Producer> {
    private static ProducerFactory instance = null;

    static {
        ConveyorFactory.getInstance().addSubFactory(ProducerFactory.getInstance());
    }

    public ProducerFactory() {
        super(Producer.class);
    }

    public static ProducerFactory getInstance() {
        if (instance == null) {
            instance = new ProducerFactory();
        }
        return instance;
    }
}