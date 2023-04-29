package com.lipsum.game.world;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.events.TileClickedEvent;

public class TileClickListener extends ClickListener {
    private final World world;

    public TileClickListener(World world) {
        this.world = world;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        super.touchDown(event, x, y, pointer, button);
        System.out.println("x: " + x + "y: " + y);
//      Get the tile...
//      World.get
        EventQueue.getInstance().invoke(new TileClickedEvent(null, new WorldCoordinate(x, y)));
        return true;
    }
}
