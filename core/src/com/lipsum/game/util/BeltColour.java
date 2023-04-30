package com.lipsum.game.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.lipsum.game.TextureStore;
import com.lipsum.game.world.WorldCoordinate;
import com.lipsum.game.world.tile.Tile;

import java.util.List;

public class BeltColour {
    public static void drawBeltColour(Batch batch, Direction direction, List<PacketType> packetTypeList, WorldCoordinate coordinate) {
        DrawUtil.drawRotated(batch, TextureStore.BELT_COLOUR_BAR, coordinate.x(), coordinate.y(), direction.rotateLeft().toDegrees(), Tile.WIDTH, Tile.HEIGHT);

        packetTypeList.stream()
                .map(BeltColour::getColourTexture)
                .forEach(tex ->
                        DrawUtil.drawRotated(batch, tex, coordinate.x(), coordinate.y(),
                                direction.rotateLeft().toDegrees(), Tile.WIDTH, Tile.HEIGHT)
                );
    }

    public static Texture getColourTexture(PacketType type) {
        return switch (type) {
            case RED -> TextureStore.BELT_COLOUR_RED;
            case BLUE -> TextureStore.BELT_COLOUR_BLUE;
            case YELLOW -> TextureStore.BELT_COLOUR_YELLOW;
        };
    }
}
