package com.lipsum.game.factory.factories;

import com.lipsum.game.entity.MovingEntity;
import com.lipsum.game.factory.AbstractFactory;
import com.lipsum.game.factory.EntityFactory;

public class MovingEntityFactory extends AbstractFactory<MovingEntity> {
    private static MovingEntityFactory instance = null;

    static {
        EntityFactory.getInstance().addSubFactory(MovingEntityFactory.getInstance());
    }

    public MovingEntityFactory() {
        super(MovingEntity.class);
    }

    public static MovingEntityFactory getInstance() {
        if (instance == null) {
            instance = new MovingEntityFactory();
        }
        return instance;
    }
}
