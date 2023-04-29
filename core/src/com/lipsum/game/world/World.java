package com.lipsum.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lipsum.game.world.tile.BackgroundTile;

import java.util.HashMap;

public class World extends Actor {
    public final int CHUNK_SIZE;

    private HashMap<Coordinate, Chunk> chunks;
    Coordinate c = new Coordinate(0,0);
    private Chunk currentChunk;
    private final Camera camera;
    private final Texture cameraTexture = new Texture("camera.png");

    public World(int chunkSize, Camera camera) {
        this.camera = camera;
        this.CHUNK_SIZE = chunkSize;
        this.chunks = new HashMap<>();
        this.currentChunk = makeBackgroundChunk(0, 0);
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
        float halfWidth = ((float)Gdx.graphics.getWidth()) / 2;
        float halfHeight = ((float)Gdx.graphics.getHeight()) / 2;

//        System.out.printf("CAMERA x:%f y:%f     CURRENTCHUNK x:%f-%f y:%f-%f%n", camera.position.x, camera.position.y, currentChunk.coordinate.x() * currentChunk.size, (currentChunk.coordinate.x() + 1) * currentChunk.size, currentChunk.coordinate.y() * currentChunk.size, (currentChunk.coordinate.y() + 1) * currentChunk.size);
        if (camera.position.x - halfWidth < currentChunk.coordinate.x() * currentChunk.size) {
            var left = new Coordinate(currentChunk.coordinate.x() - 1, currentChunk.coordinate.y());
            if (chunks.get(left) == null) {
                chunks.put(left, makeBackgroundChunk(left.x(), left.y()));
                currentChunk = chunks.get(left);
                return;
            }
        }
        if (camera.position.x + halfWidth > (currentChunk.coordinate.x() + 1) * currentChunk.size) {
            var right = new Coordinate(currentChunk.coordinate.x() + 1, currentChunk.coordinate.y());
            if (chunks.get(right) == null) {
                chunks.put(right, makeBackgroundChunk(right.x(), right.y()));
                currentChunk = chunks.get(right);
                return;
            }
        }

        if (camera.position.y - halfHeight < currentChunk.coordinate.y() * currentChunk.size) {
            var bottom = new Coordinate(currentChunk.coordinate.x(), currentChunk.coordinate.y() - 1);
            if (chunks.get(bottom) == null) {
                chunks.put(bottom, makeBackgroundChunk(bottom.x(), bottom.y()));
                currentChunk = chunks.get(bottom);
                return;
            }
        }
        if (camera.position.y + halfHeight > (currentChunk.coordinate.y() + 1) * currentChunk.size) {
            var top = new Coordinate(currentChunk.coordinate.x(), currentChunk.coordinate.y() + 1);
            if (chunks.get(top) == null) {
                chunks.put(top, makeBackgroundChunk(top.x(), top.y()));
                currentChunk = chunks.get(top);
                return;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentChunk.draw(batch);
        Coordinate c = currentChunk.coordinate;
        Coordinate[] area = new Coordinate[]{
            new Coordinate(c.x() + 1, c.y() + 1),
            new Coordinate(c.x(), c.y() + 1),
            new Coordinate(c.x() - 1, c.y() + 1),
            new Coordinate(c.x() - 1, c.y()),
            new Coordinate(c.x() + 1, c.y()),
            new Coordinate(c.x() -1, c.y() - 1),
            new Coordinate(c.x(), c.y() - 1),
            new Coordinate(c.x() + 1, c.y() - 1),
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
