package com.lipsum.game.ui.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.lipsum.game.TextureStore;
import com.lipsum.game.managers.building.catalog.BuildingMode;

/**
 * Draws a mode button
 */
public class ModeButtonDrawable implements Drawable {

    private final ButtonState state;
    private final BuildingMode mode;

    public ModeButtonDrawable(BuildingMode mode, ButtonState state) {
        this.mode = mode;
        this.state = state;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        float textureScale = 3;
        batch.draw(getBackgroundTexture(), x, y, width, height);
        Texture mtex = getModeTexture();

        // Just trust me that this centers (usually)
        batch.draw(mtex, x + width/2 - mtex.getWidth() * textureScale / 2 ,
                y + height/2 - mtex.getHeight() * textureScale / 2 ,
                mtex.getWidth() * textureScale / 2, mtex.getHeight() * textureScale / 2,
                textureScale * mtex.getWidth(), textureScale * mtex.getHeight(),
                1.0f, 1.0f, 0.0f, 0, 0, mtex.getWidth(), mtex.getHeight(), false, false);
    }

    private TextureRegion getBackgroundTexture() {
        return switch (state) {
            case DEFAULT -> BuildButtonHelper.UNPRESSED_BUTTON;
            case PRESSED -> BuildButtonHelper.PRESSED_BUTTON;
            case SELECTED -> BuildButtonHelper.SELECTED_BUTTON;
        };
    }

    private Texture getModeTexture() {
        return switch (mode) {
            case ROTATE -> TextureStore.ROTATE;
            case DELETE -> TextureStore.DELETE;
            default -> null;
        };
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
        return BuildButtonHelper.SIZE * BuildButtonHelper.SCALE;
    }

    @Override
    public void setMinWidth(float minWidth) {

    }

    @Override
    public float getMinHeight() {
        return BuildButtonHelper.SIZE * BuildButtonHelper.SCALE;
    }

    @Override
    public void setMinHeight(float minHeight) {

    }
}
