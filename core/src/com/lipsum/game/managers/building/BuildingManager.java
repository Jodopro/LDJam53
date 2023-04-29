package com.lipsum.game.managers.building;

import com.lipsum.game.event.EventConsumer;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.EventType;
import com.lipsum.game.event.events.SelectedBuildingTypeChangedEvent;
import com.lipsum.game.event.events.TileClickedEvent;
import com.lipsum.game.event.events.TileDirectionChangedEvent;
import com.lipsum.game.managers.building.catalog.BuildingCatalog;
import com.lipsum.game.managers.building.catalog.BuildingType;
import com.lipsum.game.util.Direction;
import com.lipsum.game.world.Coordinate;
import com.lipsum.game.world.tile.Tile;

/**
 * Converts a request to build into a built building (if accepted). Also checks whether the player has enough money
 */
public class BuildingManager {
    private Direction direction = Direction.EAST;
    private BuildingType selectedType = BuildingType.BELT_STRAIGHT;
    private static BuildingManager instance;

    public static BuildingManager getInstance() {
        if (instance == null) {
            instance = new BuildingManager();
        }
        return instance;
    }

    public void init() {
        EventConsumer<TileClickedEvent> tileClickedEventEventConsumer = this::onTileClickedEvent;
        EventQueue.getInstance().registerConsumer(tileClickedEventEventConsumer, EventType.TILE_CLICKED_EVENT);

        EventConsumer<TileDirectionChangedEvent> tileDirectionChangedEventEventConsumer = this::onTileRotateEvent;
        EventQueue.getInstance().registerConsumer(tileDirectionChangedEventEventConsumer,
                EventType.TILE_DIRECTION_CHANGED_EVENT);

        EventConsumer<SelectedBuildingTypeChangedEvent> selectedBuildingTypeChangedEventEventConsumer =
                this::onSelectedBuildingTypeChangedEventEvent;
        EventQueue.getInstance().registerConsumer(selectedBuildingTypeChangedEventEventConsumer,
                EventType.SELECTED_BUILDING_TYPE_CHANGED_EVENT);

    }

    public void onTileClickedEvent(TileClickedEvent tileClickedEvent) {
        try {
            int gridX = Math.floorDiv((int) tileClickedEvent.getWorldCoordinate().x(), Tile.WIDTH);
            int gridY = Math.floorDiv((int) tileClickedEvent.getWorldCoordinate().y(), Tile.HEIGHT);
            BuildingCatalog.produce(new Coordinate(gridX, gridY), direction, selectedType);
        } catch (IllegalStateException e) {
            System.out.println("Tile already occupied :(");
        }
    }

    public void onTileRotateEvent(TileDirectionChangedEvent tileDirectionChangedEvent) {
        if (tileDirectionChangedEvent.rotateRight()) {
            direction = direction.rotateRight();
        } else {
            direction = direction.rotateLeft();
        }
        System.out.println("Direction changed to " + direction);
    }

    public void onSelectedBuildingTypeChangedEventEvent(SelectedBuildingTypeChangedEvent event) {
        selectedType = event.buildingType();
        System.out.println("Selected type changed to " + selectedType);
    }
}
