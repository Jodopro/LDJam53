package com.lipsum.game.world.tile;

import com.badlogic.gdx.graphics.Texture;
import com.lipsum.game.TextureStore;
import com.lipsum.game.entities.Building;
import com.lipsum.game.world.Chunk;
import org.w3c.dom.Text;

public class Tile {
    public int x;
    public int y;
    public Chunk chunk;
    private Texture texture;
    private int type;
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

    public Tile(int x, int y, Chunk chunk, int type) {
        this.x = x;
        this.y = y;
        this.chunk = chunk;
        if (type == TileType.BACKGROUND_TILE){
            if (x == 0 || y == 0 || x == chunk.width - 1 || y == chunk.width - 1) {
                this.texture = TextureStore.edgeTile;
                return;
            }
            this.texture = TextureStore.backgroundTile;
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

    public int getType() {
        return type;
    }

    public void setType(int type){
        this.type = type;
        if (type == TileType.BACKGROUND_TILE){
            if (x == 0 || y == 0 || x == chunk.width - 1 || y == chunk.width - 1) {
                this.texture = TextureStore.edgeTile;
                return;
            }
            this.texture = TextureStore.backgroundTile;
        }
    }
}
