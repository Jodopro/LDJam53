package com.lipsum.game.factory.factories;

import com.lipsum.game.entities.Consumer;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.factory.AbstractFactory;

public class ConsumerFactory extends AbstractFactory<Consumer> {
    private static ConsumerFactory instance = null;

    static {
        ConveyorFactory.getInstance().addSubFactory(ConsumerFactory.getInstance());
    }

    public ConsumerFactory() {
        super(Consumer.class);
    }

    public static ConsumerFactory getInstance() {
        if (instance == null) {
            instance = new ConsumerFactory();
        }
        return instance;
    }
}