package com.lipsum.game.entity;

import com.lipsum.game.Util;
import com.lipsum.game.WorldProperties;
import com.lipsum.game.factory.factories.BlipFactory;
import com.lipsum.game.factory.factories.MovingEntityFactory;

import java.util.Random;

public abstract class MovingEntity extends Entity {
    protected static final Random random = new Random();
    protected float x;
    protected float y;

    protected float vx = 0;
    protected float vy = 0;


    public MovingEntity(float x, float y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        beforeMove(delta);

        x += vx * delta;
        y += vy * delta;

        for (MovingEntity entity:
             MovingEntityFactory.getInstance().getAllManagedObjects().toList()) {
            if (collides(entity)) {
                resolveCollision(entity);
            }
        }

        if (x < 0) {
            x = -x;
        }

        if (y < 0) {
            y = -y;
        }

        if (x > WorldProperties.WIDTH) {
            x = WorldProperties.WIDTH - (x - WorldProperties.WIDTH);
        }

        if (y > WorldProperties.HEIGHT) {
            y = WorldProperties.HEIGHT - (y - WorldProperties.HEIGHT);
        }
    }

    public abstract void beforeMove(float delta);

    public abstract float getRadius();

    public boolean collides(MovingEntity other) {
        float dist = Util.distance(x, y, other.x, other.y);
        return other != this && dist < (getRadius() + other.getRadius());
    }

    public void resolveCollision(MovingEntity other) {
        float dist = Util.distance(x, y, other.getX(), other.getY());
        float cos, sin;
        if (dist != 0.0) {
            cos = (x - other.getX()) / dist;
            sin = (y - other.getY()) / dist;
        } else {
            float angle = random.nextFloat((float) (Math.PI * 2));
            cos = (float) Math.cos(angle);
            sin = (float) Math.sin(angle);
        }

        float minDist = getRadius() + other.getRadius();
        x = other.x + cos * (minDist + 0.1f);
        y = other.y + sin * (minDist + 0.1f);
    }
}
