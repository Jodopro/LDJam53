package com.lipsum.game.event.events;

import com.lipsum.game.event.Event;
import com.lipsum.game.event.EventType;
import com.lipsum.game.world.WorldCoordinate;
import com.lipsum.game.world.tile.Tile;

public class TileClickedEvent implements Event {
    private final Tile tile;
    private final WorldCoordinate worldCoordinate;

    public TileClickedEvent(Tile tile, WorldCoordinate worldCoordinate) {
        this.tile = tile;
        this.worldCoordinate = worldCoordinate;
    }

    @Override
    public EventType getType() {
        return EventType.TILE_CLICKED_EVENT;
    }

    public Tile getTile() {
        return tile;
    }

    public WorldCoordinate getWorldCoordinate() {
        return worldCoordinate;
    }
}
