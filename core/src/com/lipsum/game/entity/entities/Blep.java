package com.lipsum.game.entity.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.lipsum.game.Util;
import com.lipsum.game.entity.MovingEntity;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.events.EntityDeathEvent;
import com.lipsum.game.factory.factories.BlipFactory;

import java.util.Comparator;

/**
 * Bleps try to escape the Blips (and spawn more Bleps)
 */
public class Blep extends MovingEntity {
    private final static Texture TEXTURE = new Texture("blep.png");
    private final static int SCALE = 2;
    private final static float VELOCITY = 8.0f;
    private final BlipComparator comparator = new BlipComparator();


    private boolean alive = true;
    private Blip target;

    public Blep(float x, float y) {
        super(x, y);
    }

    @Override
    public void beforeMove(float delta) {
        target = BlipFactory.getInstance().getAllManagedObjects()
                .min(comparator)
                .orElse(null);

        if (random.nextFloat() < 0.0005) {
            new Blep(x, y);
        }

        if (target != null) {
            float dist = Util.distance(x, y, target.getX(), target.getY());
            float cos = (target.getX() - x) / dist;
            float sin = (target.getY() - y) / dist;

            vx = -cos * VELOCITY;
            vy = -sin * VELOCITY;
        } else {
            vx = 0;
            vy = 0;
        }
    }

    @Override
    public float getRadius() {
        return (float) (TEXTURE.getWidth()/2 * SCALE);
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean kill() {
        if (alive) {
            alive = false;
            EventQueue.getInstance().invoke(new EntityDeathEvent(this));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(TEXTURE, x, y, TEXTURE.getWidth() * SCALE, TEXTURE.getHeight() * SCALE,
                0, 0, TEXTURE.getWidth(), TEXTURE.getHeight(), false, false);
    }

    class BlipComparator implements Comparator<Blip> {
        @Override
        public int compare(Blip blep1, Blip blep2) {
            return Float.compare(
                    Util.distance(x, y, blep1.getX(), blep1.getY()),
                    Util.distance(x, y, blep2.getX(), blep2.getY())
            );
        }
    }
}
