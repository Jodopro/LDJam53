package com.lipsum.game.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.lipsum.game.world.tile.Tile;

public class Chunk {
    public Coordinate coordinate;
    public Tile[][] tiles;

    public final int width;
    public final float size;

    private final BitmapFont font;

    public Chunk(Coordinate coordinate, Tile[][] tiles) {
        this.coordinate = coordinate;
        this.tiles = tiles;
        this.width = tiles.length;
        this.size = tiles.length * Tile.WIDTH;

        this.font = new BitmapFont();
    }

    public void draw(Batch batch) {
        for (int rowI = 0; rowI < width; rowI++) {
            for (int colI = 0; colI < width; colI++) {
                var tile = tiles[rowI][colI];
                batch.draw(tile.getTexture(), coordinate.x() * size + colI * Tile.WIDTH, coordinate.y() * size + rowI * Tile.HEIGHT);
            }
        }
        font.draw(batch, String.format("x: %d y: %d", coordinate.x(), coordinate.y()), coordinate.x() * size, coordinate.y() * size);
    }

    public void dispose() {
        for (Tile[] row : tiles) {
            for (int colI = 0; colI < width; colI++) {
                row[colI].dispose();
            }
        }
    }
}
