package com.lipsum.game.world;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.events.TileClickedEvent;
import com.lipsum.game.event.events.TileDirectionChangedEvent;

/**
 * Handles tile interactions and converts them to events for the backend code
 */
public class TileClickListener extends ClickListener {
    private final World world;

    public TileClickListener(World world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        EventQueue.getInstance().invoke(new TileClickedEvent(world.tileAt(x, y), new WorldCoordinate(x, y)));
        return true;
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        super.keyUp(event, keycode);

        if (keycode == Input.Keys.R) {
            EventQueue.getInstance().invoke(new TileDirectionChangedEvent(true));
            return true;
        }

        return false;
    }
}
