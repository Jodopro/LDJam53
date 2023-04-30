package com.lipsum.game.ui.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.lipsum.game.TextureStore;
import com.lipsum.game.managers.building.catalog.BuildingCatalog;
import com.lipsum.game.managers.building.catalog.BuildingType;

/**
 * Draws an image button with a subtitle. Should be given to the buttons as drawable
 */
public class SubtitledImageDrawable implements Drawable {

    private static final int BORDER_SIZE = 2;

    private final BitmapFont font;
    private final ButtonState state;
    private final ShapeRenderer shapeRenderer;
    private final BuildMenu buildMenu;
    private final BuildingType type;

    public SubtitledImageDrawable(BuildMenu buildMenu, BuildingType type, BitmapFont font, ButtonState state) {
        this.buildMenu = buildMenu;
        this.type = type;
        this.font = font;
        this.state = state;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        float textureScale = 3;
        batch.draw(getBackgroundTexture(), x, y, width, height);
        Texture btex = getBuildingTexture();

        float angle = buildMenu.getBuildingOrientation().rotateRight().toDegrees();
        // Just trust me that this centers (usually)
        batch.draw(btex, x + width/2 - btex.getWidth() * textureScale / 2 ,
                y + height/2 - btex.getHeight() * textureScale / 2 + font.getLineHeight() /2 ,
                btex.getWidth() * textureScale / 2, btex.getHeight() * textureScale / 2,
                textureScale * btex.getWidth(), textureScale * btex.getHeight(),
                1.0f, 1.0f, angle, 0, 0, btex.getWidth(), btex.getHeight(), false, false);
        font.draw(batch, "$" + BuildingCatalog.getCost(type), x, y + font.getLineHeight() + 2, width, Align.center, false);
    }

    private TextureRegion getBackgroundTexture() {
        return switch (state) {
            case DEFAULT -> BuildButtonHelper.UNPRESSED_BUTTON;
            case PRESSED -> BuildButtonHelper.PRESSED_BUTTON;
            case SELECTED -> BuildButtonHelper.SELECTED_BUTTON;
        };
    }

    private Texture getBuildingTexture() {
        final float time = 0.5f;
        return switch (type) {
            case BELT_STRAIGHT -> TextureStore.CONVEYOR_BELT_STRAIGHT.getKeyFrame(time, true);
            case BELT_RIGHT -> TextureStore.CONVEYOR_BELT_RIGHT.getKeyFrame(time, true);
            case BELT_LEFT -> TextureStore.CONVEYOR_BELT_LEFT.getKeyFrame(time, true);
            case MERGER -> TextureStore.MERGER.getKeyFrame(time, true);
            case SPLITTER -> TextureStore.SPLITTER.getKeyFrame(time, true);
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
