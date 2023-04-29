package com.lipsum.game.entities;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Base entity, for all entities in the game (i.e. Conveyors, I/O nodes, mergers, obstacles, packets)
 */
public abstract class Entity extends Actor {

    /**
     * Handles extra stuff on the entity when it is disposed by the factory (i.e. don't remove it from the factory here)
     */
    public abstract void onDispose();

}
