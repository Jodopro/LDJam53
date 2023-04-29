package com.lipsum.game.event.events;

import com.lipsum.game.event.Event;
import com.lipsum.game.event.EventType;
import com.lipsum.game.managers.building.catalog.BuildingType;

/**
 * Indicates that the building type selected in the UI has changed
 * @param buildingType the type of building
 */
public record SelectedBuildingTypeChangedEvent(BuildingType buildingType) implements Event {

    @Override
    public EventType getType() {
        return EventType.SELECTED_BUILDING_TYPE_CHANGED_EVENT;
    }
}
