package com.lipsum.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lipsum.game.util.TileType;
import com.lipsum.game.utils.Twople;
import com.lipsum.game.world.tile.Tile;

import java.util.HashMap;

public class World extends Actor {
    private static World instance = null;
    public static World getInstance() {
        return instance;
    }
    public static World init(int chunkSideLengthInTiles, Stage stage){
        instance = new World(chunkSideLengthInTiles, stage);
        return instance;
    }
    public final int CHUNK_DIMENSION_IN_TILES;

    private HashMap<Coordinate, Chunk> chunks;
    private final OrthographicCamera camera;
    private final Texture cameraTexture = new Texture("camera.png");
    private final TileClickListener tileClickListener;

    // ingore these variables :)
    private Coordinate currentChunkCoord;
    private Coordinate[] localChunkArea;

    public World(int chunkSideLengthInTiles, Stage stage) {
        super();
        setStage(stage);
        this.camera = (OrthographicCamera) stage.getCamera();
        this.camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        this.camera.zoom = 10f;
        this.camera.update();

        this.CHUNK_DIMENSION_IN_TILES = chunkSideLengthInTiles;
        this.chunks = new HashMap<>();
        makeBackgroundChunk(0, 0);

        tileClickListener = new TileClickListener(this);
        stage.addListener(tileClickListener);

        stage.addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                if (amountY != 0){
                    float oldZoom = camera.zoom;
                    camera.zoom *= Math.pow(1.1f, amountY);
                    camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth);
                    float factor = (1-camera.zoom/oldZoom);
                    float prefDeltaX = (x-camera.position.x)*factor;
                    float prefDeltaY = (y-camera.position.y)*factor;
                    camera.translate(prefDeltaX, prefDeltaY);
                }
                return false;
            }
        });
    }

    private Chunk makeBackgroundChunk(int x, int y) {
        var coord = new Coordinate(x, y);
        var newChunk = new Chunk(coord, new Tile[CHUNK_DIMENSION_IN_TILES][CHUNK_DIMENSION_IN_TILES]);
        for (int rowI = 0; rowI < CHUNK_DIMENSION_IN_TILES; rowI++) {
            for (int colI = 0; colI < CHUNK_DIMENSION_IN_TILES; colI++) {
                newChunk.tiles[rowI][colI] = new Tile(colI, rowI, newChunk, TileType.BACKGROUND_TILE);
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

        // batch.draw(cameraTexture, camera.position.x - 16 , camera.position.y - 16, 32, 32);
    }

//    public Chunk chunkAt(float absoluteX, float absoluteY) {
//        return chunks.get(currentChunkCoord);
//    }
//
//    public Tile tileAt(float absoluteX, float absoluteY) {
//        var chunkSize = Tile.WIDTH * CHUNK_DIMENSION_IN_TILES;
//        BiFunction<Float, Float, Float> difference = (a, b) -> {
//            var absA =  Math.abs(a);
//            var absB = Math.abs(b);
//            return absA > absB ? absA - absB : absB - absA;
//        };
//
//        int row = (int)Math.floor(Math.abs(absoluteX - (float)(currentChunkCoord.x() * chunkSize)) / Tile.WIDTH);
//        int col = (int)Math.floor(Math.abs(absoluteY - (float)(currentChunkCoord.y() * chunkSize)) / Tile.HEIGHT);
//        return chunks.get(currentChunkCoord).tiles[row][col];
//    }

//    public Coordinate chunkCoordinateOf(float absoluteX, float absoluteY){
//        return null;
//    }

    public Tile tileAt(Coordinate tileCoordinate){
        Coordinate chunk = chunkCoordinateOf(tileCoordinate);
        Coordinate local = localCoordinateOf(tileCoordinate);
        return chunks.get(chunk).tiles[local.y()][local.x()];
    }

    public Tile tileAt(float absoluteX, float absoluteY){
        return tileAt(gridCoordinateOf(absoluteX, absoluteY));
    }

    public Coordinate chunkCoordinateOf(Coordinate tileCoordinate){
        int x = (int)Math.floor(tileCoordinate.x() / (float)CHUNK_DIMENSION_IN_TILES);
        int y = (int)Math.floor(tileCoordinate.y() / (float)CHUNK_DIMENSION_IN_TILES);
        return new Coordinate(x, y);
    }

    public Coordinate localCoordinateOf(Coordinate tileCoordinate){
        int x = (int)Math.floor(tileCoordinate.x() / (float)CHUNK_DIMENSION_IN_TILES);
        int y = (int)Math.floor(tileCoordinate.y() / (float)CHUNK_DIMENSION_IN_TILES);
        return new Coordinate(tileCoordinate.x() - x*CHUNK_DIMENSION_IN_TILES, tileCoordinate.y() - y*CHUNK_DIMENSION_IN_TILES);
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
        removeListener(tileClickListener);
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.05;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.05;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            System.out.println("Button for debug");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-15*camera.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(15*camera.zoom, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -15*camera.zoom, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 15*camera.zoom, 0);
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth);
    }
}
