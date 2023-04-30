package com.lipsum.game.managers.building.catalog;

import com.lipsum.game.entities.Building;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.util.Direction;
import com.lipsum.game.world.Coordinate;

import java.util.List;

public class BuildingCatalog {
    public static int getCost(BuildingType buildingType) {
        return switch (buildingType) {
            case BELT_STRAIGHT, BELT_LEFT, BELT_RIGHT -> 10;
            case MERGER, SPLITTER -> 200;
            default -> throw new IllegalArgumentException("No price configured for building type " + buildingType);
        };
    }

    public static Building produce(Coordinate gridCoordinate, Direction direction, BuildingType buildingType) {
        return new Conveyor(gridCoordinate.x(), gridCoordinate.y(), buildingType, direction);
    }
}
