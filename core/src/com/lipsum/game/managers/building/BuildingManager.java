package com.lipsum.game.managers.building;

import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.event.EventConsumer;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.EventType;
import com.lipsum.game.event.events.*;
import com.lipsum.game.managers.building.catalog.BuildingCatalog;
import com.lipsum.game.managers.building.catalog.BuildingMode;
import com.lipsum.game.managers.building.catalog.BuildingType;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.TileType;
import com.lipsum.game.world.Coordinate;
import com.lipsum.game.world.World;
import com.lipsum.game.world.tile.Tile;

/**
 * Converts a request to build into a built building (if accepted). Also checks whether the player has enough money
 */
public class BuildingManager {
    private Direction direction = Direction.EAST;
    private BuildingType selectedType = BuildingType.BELT_STRAIGHT;
    private static BuildingManager instance;
    private BuildingMode buildingMode = BuildingMode.BUILDING;

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

        EventConsumer<SelectedModeChangedEvent> selectedModeChangedEventEventConsumer =
                this::onSelectedModeChangedEventEvent;
        EventQueue.getInstance().registerConsumer(selectedModeChangedEventEventConsumer,
                EventType.SELECTED_MODE_CHANGED_EVENT);

        EventConsumer<BuildingUpdateEvent> buildingUpdateEventEventConsumer =
                this::onBuildingUpdateEvent;
        EventQueue.getInstance().registerConsumer(buildingUpdateEventEventConsumer,
                EventType.BUILDING_UPDATE_EVENT);

    }

    public void onTileClickedEvent(TileClickedEvent tileClickedEvent) {
        int gridX = Math.floorDiv((int) tileClickedEvent.getWorldCoordinate().x(), Tile.WIDTH);
        int gridY = Math.floorDiv((int) tileClickedEvent.getWorldCoordinate().y(), Tile.HEIGHT);
        System.out.println(buildingMode);
        if (buildingMode == BuildingMode.BUILDING){
            try {
                // TODO: Check if empty (instead of this try-catch) and check cost/currency

                BuildingCatalog.produce(new Coordinate(gridX, gridY), direction, selectedType);
            } catch (IllegalStateException e) {
                System.out.println("Tile already occupied :(");
            }
        } else if (buildingMode == BuildingMode.ROTATE) {
            Tile t = World.getInstance().tileAt(new Coordinate(gridX, gridY));
            if (t.getBuilding() != null){
                t.getBuilding().rotate();
                EventQueue.getInstance().invoke(new BuildingUpdateEvent(gridX, gridY));
            }
        } else if (buildingMode == BuildingMode.DELETE) {

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

    public void onSelectedModeChangedEventEvent(SelectedModeChangedEvent event) {
        buildingMode = event.buildingMode();
    }

    public void onBuildingUpdateEvent(BuildingUpdateEvent event){
//        onUpdateNeighbour()

        World world = World.getInstance();
        Tile tile = world.tileAt(new Coordinate(event.gridX(), event.gridY()));
        if (tile.getBuilding() instanceof Conveyor){
            //TODO: seems to do weird stuff sometimes
            tile.getBuilding().onUpdateNeighbour(((Conveyor) tile.getBuilding()).getCurrentTo());
        }

        tile = world.tileAt(new Coordinate(event.gridX()+1, event.gridY()));
        if (tile.getType() == TileType.BUILDING_TILE){
            tile.getBuilding().onUpdateNeighbour(Direction.WEST);
        }
        tile = world.tileAt(new Coordinate(event.gridX()-1, event.gridY()));
        if (tile.getType() == TileType.BUILDING_TILE){
            tile.getBuilding().onUpdateNeighbour(Direction.EAST);
        }
        tile = world.tileAt(new Coordinate(event.gridX(), event.gridY()+1));
        if (tile.getType() == TileType.BUILDING_TILE){
            tile.getBuilding().onUpdateNeighbour(Direction.SOUTH);
        }
        tile = world.tileAt(new Coordinate(event.gridX(), event.gridY()-1));
        if (tile.getType() == TileType.BUILDING_TILE){
            tile.getBuilding().onUpdateNeighbour(Direction.NORTH);
        }
    }
}
