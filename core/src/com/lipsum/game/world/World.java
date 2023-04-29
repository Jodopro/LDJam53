package com.lipsum.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lipsum.game.world.tile.BackgroundTile;
import com.lipsum.game.world.tile.Tile;

import java.util.HashMap;

public class World extends Actor {
    public final int CHUNK_SIZE;

    private HashMap<Coordinate, Chunk> chunks;
    private final OrthographicCamera camera;
    private final Texture cameraTexture = new Texture("camera.png");

    private final TileClickListener tileClickListener;

    public World(int chunkSize, Stage stage) {
        super();
        setStage(stage);
        this.camera = (OrthographicCamera) stage.getCamera();
        this.camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        this.camera.zoom = 10f;
        this.camera.update();

        this.CHUNK_SIZE = chunkSize;
        this.chunks = new HashMap<>();
        makeBackgroundChunk(0, 0);

        tileClickListener = new TileClickListener(this);
        stage.addListener(tileClickListener);
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
        handleInput();
        camera.update();
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
        batch.setProjectionMatrix(camera.combined);
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
        removeListener(tileClickListener);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.2;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.2;
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
