package com.lipsum.game.world.tile;

import com.badlogic.gdx.graphics.Texture;
import com.lipsum.game.TextureStore;
import com.lipsum.game.world.Chunk;
import com.lipsum.game.world.World;

public class BackgroundTile extends Tile {
    Texture texture;

    public BackgroundTile(int x, int y, Chunk chunk) {
        super(x, y, chunk);
        if (x == 0 || y == 0 || x == chunk.width - 1 || y == chunk.width - 1) {
            this.texture = TextureStore.edgeTile;
            return;
        }
        this.texture = TextureStore.backgroundTile;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }

    @Override
    public int getType() {
        return TileType.BACKGROUND_TILE;
    }
}
