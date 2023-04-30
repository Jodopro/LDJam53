package com.lipsum.game.event.events;

import com.lipsum.game.event.Event;
import com.lipsum.game.event.EventType;
import com.lipsum.game.managers.building.catalog.BuildingMode;
import com.lipsum.game.util.PacketType;

/**
 * Indicates that the mode selected in the UI has changed
 *
 * @param buildingMode the mode
 * @param packetType
 */
public record SelectedModeChangedEvent(BuildingMode buildingMode, PacketType packetType) implements Event {

    @Override
    public EventType getType() {
        return EventType.SELECTED_MODE_CHANGED_EVENT;
    }
}
