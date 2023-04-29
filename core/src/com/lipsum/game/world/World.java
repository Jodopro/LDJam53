package com.lipsum.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lipsum.game.world.tile.BackgroundTile;
import com.lipsum.game.world.tile.Tile;

import java.util.HashMap;

public class World extends Actor {
    public final int CHUNK_SIZE;

    private HashMap<Coordinate, Chunk> chunks;
    private final Camera camera;
    private final Texture cameraTexture = new Texture("camera.png");

    public World(int chunkSize, Camera camera) {
        this.camera = camera;
        this.CHUNK_SIZE = chunkSize;
        this.chunks = new HashMap<>();
        makeBackgroundChunk(0, 0);
    }

    private Chunk makeBackgroundChunk(int x, int y) {
        var coord = new Coordinate(x, y);
        var newChunk = new Chunk(coord, new BackgroundTile[CHUNK_SIZE][CHUNK_SIZE]);
        for (int rowI = 0; rowI < CHUNK_SIZE; rowI++) {
            for (int colI = 0; colI < CHUNK_SIZE; colI++) {
                newChunk.tiles[rowI][colI] = new BackgroundTile(colI, rowI, this);
            }
        }
        chunks.put(coord, newChunk);
        System.out.printf("made a chunk at %d %d %n", x, y);
        return newChunk;
    }

    public void step() {
        var chunkSize = Tile.WIDTH * CHUNK_SIZE;
        Coordinate estimatedCoord = new Coordinate((int)(camera.position.x / chunkSize), (int)(camera.position.y / chunkSize));
        Coordinate[] area = new Coordinate[]{
                new Coordinate(estimatedCoord.x() + 1, estimatedCoord.y() + 1),
                new Coordinate(estimatedCoord.x(), estimatedCoord.y() + 1),
                new Coordinate(estimatedCoord.x() - 1, estimatedCoord.y() + 1),
                new Coordinate(estimatedCoord.x() - 1, estimatedCoord.y()),
                new Coordinate(estimatedCoord.x(), estimatedCoord.y()),
                new Coordinate(estimatedCoord.x() + 1, estimatedCoord.y()),
                new Coordinate(estimatedCoord.x() -1, estimatedCoord.y() - 1),
                new Coordinate(estimatedCoord.x(), estimatedCoord.y() - 1),
                new Coordinate(estimatedCoord.x() + 1, estimatedCoord.y() - 1),
        };

        for (var n : area) {
            if (chunks.get(n) == null) {
                chunks.put(n, makeBackgroundChunk(n.x(), n.y()));
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        var chunkSize = Tile.WIDTH * CHUNK_SIZE;
        Coordinate estimatedCoord = new Coordinate((int)(camera.position.x / chunkSize), (int)(camera.position.y / chunkSize));
        Coordinate[] area = new Coordinate[]{
            new Coordinate(estimatedCoord.x() + 1, estimatedCoord.y() + 1),
            new Coordinate(estimatedCoord.x(), estimatedCoord.y() + 1),
            new Coordinate(estimatedCoord.x() - 1, estimatedCoord.y() + 1),
            new Coordinate(estimatedCoord.x() - 1, estimatedCoord.y()),
            new Coordinate(estimatedCoord.x(), estimatedCoord.y()),
            new Coordinate(estimatedCoord.x() + 1, estimatedCoord.y()),
            new Coordinate(estimatedCoord.x() -1, estimatedCoord.y() - 1),
            new Coordinate(estimatedCoord.x(), estimatedCoord.y() - 1),
            new Coordinate(estimatedCoord.x() + 1, estimatedCoord.y() - 1),
        };

        for (var n : area) {
            if (chunks.get(n) != null) {
                chunks.get(n).draw(batch);
            }
        }

        batch.draw(cameraTexture, camera.position.x - 16 , camera.position.y - 16, 32, 32);
    }

    public void dispose() {
        for (var coord : chunks.keySet()) {
            chunks.get(coord).dispose();
        }
    }
}
