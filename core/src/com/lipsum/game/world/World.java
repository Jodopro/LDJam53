package com.lipsum.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lipsum.game.world.tile.BackgroundTile;
import com.lipsum.game.world.tile.Tile;

import java.util.HashMap;

public class World extends Actor {
    public final int CHUNK_SIZE;

    private HashMap<Coordinate, Chunk> chunks;
    private final OrthographicCamera camera;
    private final Texture cameraTexture = new Texture("camera.png");

    // ingore these variables :)
    private Coordinate estimatedCoord;
    private Coordinate[] localArea;

    public World(int chunkSize, OrthographicCamera camera) {
        this.camera = camera;
        this.camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        this.camera.zoom = 10f;
        this.camera.update();

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
        System.out.printf("made a chankoe @ %d : %d%n", x, y);
        return newChunk;
    }

    public void step() {
        var chunkSize = Tile.WIDTH * CHUNK_SIZE;
        estimatedCoord = new Coordinate((int)Math.floor(camera.position.x / chunkSize), (int)Math.floor(camera.position.y / chunkSize));

        handleInput();
        camera.update();
        localArea = new Coordinate[]{
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

        for (var n : localArea) {
            if (chunks.get(n) == null) {
                chunks.put(n, makeBackgroundChunk(n.x(), n.y()));
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setProjectionMatrix(camera.combined);

        for (var n : localArea) {
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
