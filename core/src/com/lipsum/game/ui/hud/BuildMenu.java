package com.lipsum.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Renders and handles the build menu
 */
public class BuildMenu extends Table {
    public static final Color BACKGROUND_COLOUR = new Color(0.2f, 0.2f, 0.2f, 1.0f);

    public BuildMenu() {
        ButtonGroup<BuildButton> buttonGroup = new ButtonGroup<>();

        buttonGroup.add(new BuildButton(new Texture(Gdx.files.internal("textures/temp-building-sprite.png"))));
        buttonGroup.add(new BuildButton(new Texture(Gdx.files.internal("textures/temp-building-sprite.png"))));
        buttonGroup.add(new BuildButton(new Texture(Gdx.files.internal("textures/temp-building-sprite.png"))));

        Label.LabelStyle style = new Label.LabelStyle(getFont(), Color.WHITE);
        add(new Label("Build menu:", style)).expandX();
        row();

        Table subTable = new Table();
        buttonGroup.getButtons().forEach(subTable::add);
        add(subTable);
    }

    public static BitmapFont getFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FreeMono.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        return font;
    }
}
