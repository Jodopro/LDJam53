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
        int x = gridCoordinate.x();
        int y = gridCoordinate.y();

        return switch (buildingType) {
            case BELT_STRAIGHT -> new Conveyor(x ,y, direction);
            case BELT_LEFT -> new Conveyor(x, y, List.of(direction), List.of(direction.rotateRight()));
            case BELT_RIGHT -> new Conveyor(x, y, List.of(direction), List.of(direction.rotateLeft()));
            case MERGER -> new Conveyor(x, y, List.of(direction),
                    List.of(direction.rotateLeft(), direction.opposite(), direction.rotateRight()));
            case SPLITTER -> new Conveyor(x, y, List.of(direction, direction.rotateLeft(), direction.rotateRight()),
                    List.of(direction.opposite()));
            default -> throw new IllegalArgumentException("No price configured for building type " + buildingType);
        };
    }
}
