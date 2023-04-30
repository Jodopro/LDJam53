package com.lipsum.game.managers.building;

import com.badlogic.gdx.Input;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.event.EventConsumer;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.EventType;
import com.lipsum.game.event.events.*;
import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.managers.building.catalog.BuildingCatalog;
import com.lipsum.game.managers.building.catalog.BuildingMode;
import com.lipsum.game.managers.building.catalog.BuildingType;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.PacketType;
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
    private PacketType packetType;

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
            Tile t = World.getInstance().tileAt(new Coordinate(gridX, gridY));
            if (t.getBuilding() != null){
                System.out.println("Tile already occupied :(");
            } else {
                BuildingCatalog.produce(new Coordinate(gridX, gridY), direction, selectedType);
                EventQueue.getInstance().invoke(new BuildingUpdateEvent(gridX, gridY));
            }
        } else if (buildingMode == BuildingMode.ROTATE) {
            Tile t = World.getInstance().tileAt(new Coordinate(gridX, gridY));
            if (t.getBuilding() != null){
                t.getBuilding().rotate();
                EventQueue.getInstance().invoke(new BuildingUpdateEvent(gridX, gridY));
            }
        } else if (buildingMode == BuildingMode.DELETE) {
            Tile t = World.getInstance().tileAt(new Coordinate(gridX, gridY));
            if (t.getBuilding() != null){
                EntityFactory.getInstance().removeManagedObject(t.getBuilding());
                t.setBuilding(null);
                t.setType(TileType.BACKGROUND_TILE);
                EventQueue.getInstance().invoke(new BuildingUpdateEvent(gridX, gridY));
            }

        } else if (buildingMode == BuildingMode.COLOUR && packetType != null) {
            Tile t = World.getInstance().tileAt(new Coordinate(gridX, gridY));
            if (t.getBuilding() instanceof Conveyor c) {
                if (tileClickedEvent.getButton() == Input.Buttons.LEFT) {
                    c.addPacketType(packetType);
                    EventQueue.getInstance().invoke(new BuildingUpdateEvent(gridX, gridY));
                } else {
                    c.removePacketType(packetType);
                    EventQueue.getInstance().invoke(new BuildingUpdateEvent(gridX, gridY));
                }
            }

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
        packetType = event.packetType();
    }

    public void onBuildingUpdateEvent(BuildingUpdateEvent event){
//        onUpdateNeighbour()

        World world = World.getInstance();
        Tile tile = world.tileAt(new Coordinate(event.gridX(), event.gridY()));
//        if (tile.getBuilding() instanceof Conveyor){
//            //TODO: seems to do weird stuff sometimes
//            ((Conveyor) tile.getBuilding()).onUpdateSelf();
//        }

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
