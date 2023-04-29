package com.lipsum.game.factory.factories;

import com.lipsum.game.entity.Entity;
import com.lipsum.game.factory.AbstractFactory;
import com.lipsum.game.factory.EntityDeathManager;

public class EntityFactory extends AbstractFactory<Entity> {
    private static EntityFactory instance = null;

    public EntityFactory() {
        super(Entity.class);
        EntityDeathManager.init();
    }

    public static EntityFactory getInstance() {
        if (instance == null) {
            instance = new EntityFactory();
        }
        return instance;
    }

    @Override
    public boolean removeManagedObject(Entity managedObject) {
        managedObject.onDispose();
        return super.removeManagedObject(managedObject);
    }

    @Override
    public void removeManagedObjects() {
        getAllManagedObjects().forEach(Entity::onDispose);
        super.removeManagedObjects();
    }
}