package com.lipsum.game.entity;

/**
 * Base entity, for all entities in the game (i.e. Conveyors, I/O nodes, mergers, obstacles, packets)
 */
public abstract class Entity {

    /**
     * Handles extra stuff on the entity when it is disposed by the factory (i.e. don't remove it from the factory here)
     */
    public abstract void onDispose();

}
