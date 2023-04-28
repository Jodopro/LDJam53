package com.lipsum.game.entity;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lipsum.game.factory.EntityFactory;
import com.lipsum.game.rendering.EntityRenderer;

public abstract class Entity extends Actor {

    public Entity() {
        EntityFactory.getInstance().addManagedObject(this);
        // Add entity as actor
        EntityRenderer.getInstance().getStage().addActor(this);
    }

    public void onDispose() {
        // Remove entity as actor
        EntityRenderer.getInstance().getStage().getRoot().removeActor(this);
    }

}
