package com.lipsum.game.ui.styles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class DefaultLabelStyle {
    private static Label.LabelStyle instance;
    private static BitmapFont font;

    public static Label.LabelStyle getLabelStyle() {
        if (instance == null) {
            instance = new Label.LabelStyle(getFont(), Color.BLACK);
        }
        return instance;
    }

    public static BitmapFont getFont() {
        if (font == null) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FreeMono.otf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = 12;
            parameter.color = Color.BLACK;
            font = generator.generateFont(parameter); // font size 12 pixels
            generator.dispose(); // don't forget to dispose to avoid memory leaks!
        }
        return font;
    }




}
