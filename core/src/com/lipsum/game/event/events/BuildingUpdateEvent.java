package com.lipsum.game.event.events;

import com.lipsum.game.event.Event;
import com.lipsum.game.event.EventType;
import com.lipsum.game.managers.building.catalog.BuildingMode;

/**
 * Indicates that a building has updated in a specific place
 * @param gridX the X coordinate
 * @param gridY the Y coordinate
 */
public record BuildingUpdateEvent(int gridX, int gridY) implements Event {
    @Override
    public EventType getType() {
        return EventType.BUILDING_UPDATE_EVENT;
    }
}
