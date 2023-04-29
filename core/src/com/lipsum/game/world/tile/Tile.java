package com.lipsum.game.world.tile;

import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

public abstract class Tile {
    public int x;
    public int y;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    public abstract Texture getTexture();
    public abstract void dispose();

    Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
