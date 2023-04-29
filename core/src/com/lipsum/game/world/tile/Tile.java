package com.lipsum.game.world.tile;

import com.badlogic.gdx.graphics.Texture;
import com.lipsum.game.world.Chunk;
import org.w3c.dom.Text;

public abstract class Tile {
    public int x;
    public int y;
    public Chunk chunk;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public abstract Texture getTexture();

    public abstract void dispose();
    public abstract int getType();

    Tile(int x, int y, Chunk chunk) {
        this.x = x;
        this.y = y;
        this.chunk = chunk;
    }
}
