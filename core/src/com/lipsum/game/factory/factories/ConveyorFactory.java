package com.lipsum.game.factory.factories;

import com.lipsum.game.entities.Building;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.factory.AbstractFactory;

public class ConveyorFactory extends AbstractFactory<Conveyor> {
    private static ConveyorFactory instance = null;

    static {
        BuildingFactory.getInstance().addSubFactory(ConveyorFactory.getInstance());
    }

    public ConveyorFactory() {
        super(Conveyor.class);
    }

    public static ConveyorFactory getInstance() {
        if (instance == null) {
            instance = new ConveyorFactory();
        }
        return instance;
    }
}