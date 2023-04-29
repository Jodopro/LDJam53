package com.lipsum.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lipsum.game.utils.Twople;
import com.lipsum.game.world.tile.BackgroundTile;
import com.lipsum.game.world.tile.Tile;

import java.util.HashMap;
import java.util.function.BiFunction;

public class World extends Actor {
    public final int CHUNK_DIMENSION_IN_TILES;

    private HashMap<Coordinate, Chunk> chunks;
    private final OrthographicCamera camera;
    private final Texture cameraTexture = new Texture("camera.png");

    // ingore these variables :)
    private Coordinate currentChunkCoord;
    private Coordinate[] localChunkArea;

    public World(int chunkSideLengthInTiles, OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        this.camera.zoom = 10f;
        this.camera.update();

        this.CHUNK_DIMENSION_IN_TILES = chunkSideLengthInTiles;
        this.chunks = new HashMap<>();
        makeBackgroundChunk(0, 0);
    }

    private Chunk makeBackgroundChunk(int x, int y) {
        var coord = new Coordinate(x, y);
        var newChunk = new Chunk(coord, new BackgroundTile[CHUNK_DIMENSION_IN_TILES][CHUNK_DIMENSION_IN_TILES]);
        for (int rowI = 0; rowI < CHUNK_DIMENSION_IN_TILES; rowI++) {
            for (int colI = 0; colI < CHUNK_DIMENSION_IN_TILES; colI++) {
                newChunk.tiles[rowI][colI] = new BackgroundTile(colI, rowI, newChunk);
            }
        }
        chunks.put(coord, newChunk);
        System.out.printf("made a chankoe @ %d : %d%n", x, y);
        return newChunk;
    }

    public void step() {
        var chunkSize = Tile.WIDTH * CHUNK_DIMENSION_IN_TILES;
        currentChunkCoord = new Coordinate((int)Math.floor(camera.position.x / chunkSize), (int)Math.floor(camera.position.y / chunkSize));

        handleInput();
        camera.update();
        localChunkArea = new Coordinate[]{
            new Coordinate(currentChunkCoord.x() + 1, currentChunkCoord.y() + 1),
            new Coordinate(currentChunkCoord.x(), currentChunkCoord.y() + 1),
            new Coordinate(currentChunkCoord.x() - 1, currentChunkCoord.y() + 1),
            new Coordinate(currentChunkCoord.x() - 1, currentChunkCoord.y()),
            new Coordinate(currentChunkCoord.x(), currentChunkCoord.y()),
            new Coordinate(currentChunkCoord.x() + 1, currentChunkCoord.y()),
            new Coordinate(currentChunkCoord.x() -1, currentChunkCoord.y() - 1),
            new Coordinate(currentChunkCoord.x(), currentChunkCoord.y() - 1),
            new Coordinate(currentChunkCoord.x() + 1, currentChunkCoord.y() - 1),
        };

        for (var n : localChunkArea) {
            if (chunks.get(n) == null) {
                chunks.put(n, makeBackgroundChunk(n.x(), n.y()));
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(camera.combined);

        for (var n : localChunkArea) {
            if (chunks.get(n) != null) {
                chunks.get(n).draw(batch);
            }
        }

        batch.draw(cameraTexture, camera.position.x - 16 , camera.position.y - 16, 32, 32);
    }

    public Chunk chunkAt(float absoluteX, float absoluteY) {
        return chunks.get(currentChunkCoord);
    }

    public Tile tileAt(float absoluteX, float absoluteY) {
        var chunkSize = Tile.WIDTH * CHUNK_DIMENSION_IN_TILES;
        BiFunction<Float, Float, Float> difference = (a, b) -> {
            var absA =  Math.abs(a);
            var absB = Math.abs(b);
            return absA > absB ? absA - absB : absB - absA;
        };

        int row = (int)Math.floor(difference.apply(absoluteX, (float)(currentChunkCoord.x() * chunkSize)) / Tile.WIDTH);
        int col = (int)Math.floor(difference.apply(absoluteY, (float)(currentChunkCoord.y() * chunkSize)) / Tile.HEIGHT);
        return chunks.get(currentChunkCoord).tiles[row][col];
    }

    public Coordinate gridCoordinateOf(float absoluteX, float absoluteY) {
        int x = (int)Math.floor(absoluteX / Tile.WIDTH);
        int y = (int)Math.floor(absoluteY / Tile.HEIGHT);
        return new Coordinate(x, y);
    }

    public Twople<Float, Float> absolutePositionOf(Tile tile) {
        var chunkSize = Tile.WIDTH * CHUNK_DIMENSION_IN_TILES;
        float x = tile.chunk.coordinate.x() * chunkSize + tile.x * Tile.WIDTH;
        float y = tile.chunk.coordinate.y() * chunkSize + tile.y * Tile.HEIGHT;
        return new Twople<>(x, y);
    }

    public void dispose() {
        for (var coord : chunks.keySet()) {
            chunks.get(coord).dispose();
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.05;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.05;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-9, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(9, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -9, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 9, 0);
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth);
    }
}
