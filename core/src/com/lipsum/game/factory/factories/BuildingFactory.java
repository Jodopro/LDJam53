package com.lipsum.game.factory.factories;

import com.lipsum.game.entities.Building;
import com.lipsum.game.factory.AbstractFactory;
import com.lipsum.game.managers.BuildingManager;

public class BuildingFactory extends AbstractFactory<Building> {
    private static BuildingFactory instance = null;

    static {
        EntityFactory.getInstance().addSubFactory(BuildingFactory.getInstance());
        BuildingManager.init();
    }

    public BuildingFactory() {
        super(Building.class);
    }

    public static BuildingFactory getInstance() {
        if (instance == null) {
            instance = new BuildingFactory();
        }
        return instance;
    }
}