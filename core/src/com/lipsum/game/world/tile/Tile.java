package com.lipsum.game.world.tile;

import com.badlogic.gdx.graphics.Texture;
import com.lipsum.game.TextureStore;
import com.lipsum.game.entities.Building;
import com.lipsum.game.util.TileType;
import com.lipsum.game.world.Chunk;

public class Tile {
    public int x;
    public int y;
    public Chunk chunk;
    private Texture texture;
    private TileType type;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    private Building building;

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public void dispose() {
        this.texture.dispose();
    }

    public Tile(int x, int y, Chunk chunk, TileType type) {
        this.x = x;
        this.y = y;
        this.chunk = chunk;
        if (type == TileType.BACKGROUND_TILE){
            if (x == 0 || y == 0 || x == chunk.width - 1 || y == chunk.width - 1) {
                this.texture = TextureStore.GRASS_TILE;
                return;
            }
            this.texture = TextureStore.GRASS_TILE;
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Texture getTexture() {
        return texture;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type){
        this.type = type;
        if (type == TileType.BACKGROUND_TILE){
            if (x == 0 || y == 0 || x == chunk.width - 1 || y == chunk.width - 1) {
                this.texture = TextureStore.GRASS_TILE;
                return;
            }
            this.texture = TextureStore.GRASS_TILE;
        }
    }
}
