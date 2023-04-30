package com.lipsum.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.lipsum.game.LDJam53;
import com.lipsum.game.world.Coordinate;
import com.lipsum.game.world.World;
import com.lipsum.game.world.tile.Tile;
import com.lipsum.game.util.TileType;

import static java.lang.Math.abs;

public class Building extends Entity{
    protected int gridX;
    protected int gridY;
    protected Building northBuilding;
    protected Building southBuilding;
    protected Building eastBuilding;
    protected Building westBuilding;
    public Building(int x, int y){
        this.gridX = x;
        this.gridY = y;
        setBounds(x*Tile.WIDTH, y*Tile.WIDTH, Tile.WIDTH, Tile.WIDTH);

        World world = World.getInstance();
        Tile tile = world.tileAt(new Coordinate(x, y));
        tile.setBuilding(this);
        if (tile.getType() == TileType.BUILDING_TILE){
            throw new IllegalStateException("tile already has a building");
        }
        tile.setType(TileType.BUILDING_TILE);

        tile = world.tileAt(new Coordinate(x+1, y));
        if (tile.getType() == TileType.BUILDING_TILE){
            tile.getBuilding().westBuilding = this;
            this.eastBuilding = tile.getBuilding();
        }
        tile = world.tileAt(new Coordinate(x-1, y));
        if (tile.getType() == TileType.BUILDING_TILE){
            tile.getBuilding().eastBuilding = this;
            this.westBuilding = tile.getBuilding();
        }
        tile = world.tileAt(new Coordinate(x, y+1));
        if (tile.getType() == TileType.BUILDING_TILE){
            tile.getBuilding().southBuilding = this;
            this.northBuilding = tile.getBuilding();
        }
        tile = world.tileAt(new Coordinate(x, y-1));
        if (tile.getType() == TileType.BUILDING_TILE){
            tile.getBuilding().northBuilding = this;
            this.southBuilding = tile.getBuilding();
        }
        LDJam53.machineGroup.addActor(this);
    }
    @Override
    public void onDispose() {
        remove();
    }

    protected ShapeRenderer renderer = new ShapeRenderer();
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(getColor());
        renderer.rect(0, 0, getWidth(), getHeight());
        renderer.end();

        batch.begin();
    }
}
