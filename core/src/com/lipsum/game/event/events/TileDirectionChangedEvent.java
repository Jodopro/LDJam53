package com.lipsum.game.event.events;

import com.lipsum.game.event.Event;
import com.lipsum.game.event.EventType;

public record TileDirectionChangedEvent(boolean rotateRight) implements Event {

    @Override
    public EventType getType() {
        return EventType.TILE_DIRECTION_CHANGED_EVENT;
    }
}
