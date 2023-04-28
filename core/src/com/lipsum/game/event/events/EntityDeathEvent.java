package com.lipsum.game.event.events;

import com.lipsum.game.entity.Entity;
import com.lipsum.game.event.Event;
import com.lipsum.game.event.EventType;

public class EntityDeathEvent implements Event {
    private final Entity entity;

    public EntityDeathEvent(Entity entity) {
        this.entity = entity;
    }

    @Override
    public EventType getType() {
        return EventType.ENTITY_DEATH_EVENT;
    }

    public Entity getEntity() {
        return entity;
    }
}
