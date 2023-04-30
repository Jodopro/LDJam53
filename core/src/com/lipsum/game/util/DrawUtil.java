package com.lipsum.game.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DrawUtil {

    /**
     * Draws a rotated texture, rotated around the center of the texture.
     * The texture is placed such that the top left is on x,y.
     */
    public static void drawRotated(Batch batch, Texture texture, float x, float y, float angle, float width, float height) {
        int texWidth = texture.getWidth();
        int texHeight = texture.getHeight();
        float originX = width / 2.0f;
        float originY = height / 2.0f;
        batch.draw(texture, x, y, originX, originY, width, height, 1.0f, 1.0f, angle, 0, 0, texWidth, texHeight, false, false);
    }
}
