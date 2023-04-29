package com.lipsum.game.managers;

import com.lipsum.game.entities.Building;
import com.lipsum.game.event.EventConsumer;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.EventType;
import com.lipsum.game.event.events.TileClickedEvent;
import com.lipsum.game.world.tile.Tile;

/**
 * Converts a request to build into a built building (if accepted). Also checks whether the player has enough money
 */
public class BuildingManager {
    public static void init() {
        EventConsumer<TileClickedEvent> tileClickedEventEventConsumer = BuildingManager::onTileClickedEvent;
        EventQueue.getInstance().registerConsumer(tileClickedEventEventConsumer, EventType.TILE_CLICKED_EVENT);
    }

    public static void onTileClickedEvent(TileClickedEvent tileClickedEvent) {
        System.out.println("Tile clicked :)");
        new Building(
                (int) (tileClickedEvent.getWorldCoordinate().x() / Tile.WIDTH),
                (int) (tileClickedEvent.getWorldCoordinate().y() / Tile.HEIGHT)
        );
    }
}
