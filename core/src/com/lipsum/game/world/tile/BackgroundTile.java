package com.lipsum.game.world.tile;

import com.badlogic.gdx.graphics.Texture;
import com.lipsum.game.TextureStore;
import com.lipsum.game.world.World;

public class BackgroundTile extends Tile {
    Texture texture;

    public BackgroundTile(int x, int y, World world) {
        super(x, y);
        if (x == 0 || y == 0 || x == world.CHUNK_SIZE - 1 || y == world.CHUNK_SIZE - 1) {
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
}
