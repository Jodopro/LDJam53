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
import com.badlogic.gdx.utils.Null;

public class SubtitledImageDrawable implements Drawable {
    private final Texture texture;
    private final String text;
    private final BitmapFont font;
    private final Color overlay;
    private final ShapeRenderer shapeRenderer;

    public SubtitledImageDrawable(Texture texture, String text, BitmapFont font, @Null Color overlay) {
        this.texture = texture;
        this.text = text;
        this.font = font;
        this.overlay = overlay;
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
        batch.draw(texture, x, y + font.getLineHeight(), textureScale * texture.getWidth(), textureScale * texture.getHeight());
        font.draw(batch, text, x, y + font.getLineHeight(), width, Align.center, false);

        if (overlay != null) {
            batch.end();

            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(overlay);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);


            batch.begin();
        }
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
