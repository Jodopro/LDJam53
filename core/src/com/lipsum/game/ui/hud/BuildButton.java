package com.lipsum.game.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import static com.lipsum.game.ui.hud.BuildMenu.getFont;

public class BuildButton extends Button {

    private final Texture texture;

    public BuildButton(Texture texture) {
        super(new ButtonStyle(
                new SubtitledImageDrawable(texture, "1000", getFont(), ButtonState.DEFAULT),
                new SubtitledImageDrawable(texture, "1000", getFont(), ButtonState.PRESSED),
                new SubtitledImageDrawable(texture, "1000", getFont(), ButtonState.SELECTED)

        ));

        this.texture = texture;
    }

    @Override
    public float getPrefWidth() {
        if (texture == null) {
            return 0;
        }
        return texture.getWidth() * 4;
    }

    @Override
    public float getPrefHeight() {
        if (texture == null) {
            return 0;
        }
        return texture.getHeight() * 4 + getFont().getLineHeight();
    }
}
