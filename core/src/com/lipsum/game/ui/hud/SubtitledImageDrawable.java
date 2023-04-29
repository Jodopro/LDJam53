package com.lipsum.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

/**
 * Draws an image button with a subtitle. Should be given to the buttons as drawable
 */
public class SubtitledImageDrawable implements Drawable {

    private static final Color OVERLAY_COLOUR_PRESSED = new Color(0.0f, 0.0f, 0.0f, 0.3f);
    private static final Color OVERLAY_COLOUR_CHECKED = new Color(0.0f, 0.0f, 0.0f, 0.1f);

    private static final Color BORDER_LIGHT = new Color(1.0f, 1.0f, 1.0f, 0.1f);
    private static final Color BORDER_DARK = new Color(0.0f, 0.0f, 0.0f, 0.1f);
    private static final Color BORDER_CHECKED = new Color(1.0f, 0.0f, 0.0f, 0.1f);

    private static final int BORDER_SIZE = 2;


    private final Texture texture;
    private final String text;
    private final BitmapFont font;
    private final ButtonState state;
    private final ShapeRenderer shapeRenderer;

    public SubtitledImageDrawable(Texture texture, String text, BitmapFont font, ButtonState state) {
        this.texture = texture;
        this.text = text;
        this.font = font;
        this.state = state;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        float textureScale = width / texture.getWidth();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(BuildMenu.BACKGROUND_COLOUR);
        shapeRenderer.rect(x, y, width, height);
        shapeRenderer.end();


        batch.begin();
        batch.draw(texture, x, y + font.getLineHeight() * 2, textureScale * texture.getWidth(), textureScale * texture.getHeight());
        font.draw(batch, text, x, y + font.getLineHeight() * 2, width, Align.center, false);

        if (state != ButtonState.DEFAULT) {
            batch.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            // Overlay (darkens image)
            shapeRenderer.setColor(state == ButtonState.PRESSED ? OVERLAY_COLOUR_PRESSED : OVERLAY_COLOUR_CHECKED);
            shapeRenderer.rect(x, y, width, height);

            // Borders
            drawBorders(shapeRenderer, x, y, width, height);

            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);


            batch.begin();
        }
    }

    private void drawBorders(ShapeRenderer shapeRenderer, float x, float y, float width, float height) {
        Color color1 = switch (state) {
            case DEFAULT -> BORDER_LIGHT;
            case PRESSED -> BORDER_DARK;
            case SELECTED -> BORDER_CHECKED;
        };
        Color color2 = switch (state) {
            case DEFAULT -> BORDER_DARK;
            case PRESSED -> BORDER_LIGHT;
            case SELECTED -> BORDER_CHECKED;
        };

        shapeRenderer.setColor(color1);
        shapeRenderer.rect(x, y, BORDER_SIZE, height);
        shapeRenderer.rect(x + BORDER_SIZE, y, width - BORDER_SIZE * 2, BORDER_SIZE);

        shapeRenderer.setColor(color2);
        shapeRenderer.rect(x + width - BORDER_SIZE, y, BORDER_SIZE, height);
        shapeRenderer.rect(x + BORDER_SIZE, y + height - BORDER_SIZE, width - BORDER_SIZE * 2, BORDER_SIZE);
    }

    @Override
    public float getLeftWidth() {
        return 0;
    }

    @Override
    public void setLeftWidth(float leftWidth) {

    }

    @Override
    public float getRightWidth() {
        return 0;
    }

    @Override
    public void setRightWidth(float rightWidth) {

    }

    @Override
    public float getTopHeight() {
        return 0;
    }

    @Override
    public void setTopHeight(float topHeight) {

    }

    @Override
    public float getBottomHeight() {
        return 0;
    }

    @Override
    public void setBottomHeight(float bottomHeight) {

    }

    @Override
    public float getMinWidth() {
        return texture.getWidth();
    }

    @Override
    public void setMinWidth(float minWidth) {

    }

    @Override
    public float getMinHeight() {
        return texture.getHeight() + font.getLineHeight();
    }

    @Override
    public void setMinHeight(float minHeight) {

    }
}
