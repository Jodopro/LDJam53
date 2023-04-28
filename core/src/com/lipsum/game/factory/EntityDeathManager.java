package com.lipsum.game.factory;

import com.lipsum.game.event.EventConsumer;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.EventType;
import com.lipsum.game.event.events.EntityDeathEvent;

public class EntityDeathManager {

    private EntityDeathManager() {
        // Private constructor to avoid instantiation
    }

    public static void init() {
        EventConsumer<EntityDeathEvent> entityDeathEventEventConsumer = EntityDeathManager::onEntityDeathEvent;
        EventQueue.getInstance().registerConsumer(entityDeathEventEventConsumer, EventType.ENTITY_DEATH_EVENT);
    }

    public static void onEntityDeathEvent(EntityDeathEvent entityDeathEvent) {
        EntityFactory.getInstance().removeManagedObject(entityDeathEvent.getEntity());
    }
}