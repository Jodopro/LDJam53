package com.lipsum.game.entity.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.lipsum.game.Util;
import com.lipsum.game.entity.MovingEntity;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.events.EntityDeathEvent;
import com.lipsum.game.factory.factories.BlepFactory;

import java.util.Comparator;

/**
 * Blips hunt and eat Bleps
 */
public class Blip extends MovingEntity {
    private final static Texture TEXTURE = new Texture("blip.png");
    private final static int SCALE = 2;


    private static final float VELOCITY = 20.0f;
    private final BlepComparator comparator = new BlepComparator();

    public static final float HUNGER_DECAY_RATE = 0.5f;
    public double hunger = 1.0;

    private Blep target;

    public Blip(float x, float y) {
        super(x, y);
    }

    @Override
    public void beforeMove(float delta) {
        hunger -= HUNGER_DECAY_RATE * delta;

        if (target != null && Util.distance(x, y, target.getX(), target.getY()) < getRadius() + target.getRadius() + 1.0f) {
            if (target.kill()) {
                hunger += 1.0f;
            }

            target = null;
        }

        if (hunger > 10.0f) {
            hunger -= 5.0f;
            new Blip(x, y);
        }

        if (hunger < 0.0) {
            EventQueue.getInstance().invoke(new EntityDeathEvent(this));
            System.out.println("Ded");
        }

        target = BlepFactory.getInstance().getAllManagedObjects()
                .filter(Blep::isAlive)
                .min(comparator)
                .orElse(null);

        if (target != null) {
            float dist = Util.distance(x, y, target.getX(), target.getY());
            float cos = (target.getX() - x) / dist;
            float sin = (target.getY() - y) / dist;

            vx = cos * VELOCITY;
            vy = sin * VELOCITY;
        } else {
            vx = 0;
            vy = 0;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(TEXTURE, x, y, TEXTURE.getWidth() * SCALE, TEXTURE.getHeight() * SCALE,
                0, 0, TEXTURE.getWidth(), TEXTURE.getHeight(), false, false);
    }

    @Override
    public float getRadius() {
        return (float) (TEXTURE.getWidth()/2 * SCALE);
    }

    class BlepComparator implements Comparator<Blep> {

        @Override
        public int compare(Blep blep1, Blep blep2) {
            return Float.compare(
                    Util.distance(x, y, blep1.getX(), blep1.getY()),
                    Util.distance(x, y, blep2.getX(), blep2.getY())
            );
        }
    }
}
