package com.lipsum.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.lipsum.game.event.EventConsumer;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.EventType;
import com.lipsum.game.event.events.TileDirectionChangedEvent;
import com.lipsum.game.managers.building.catalog.BuildingMode;
import com.lipsum.game.managers.building.catalog.BuildingType;
import com.lipsum.game.util.Direction;

/**
 * Renders and handles the build menu
 */
public class BuildMenu extends Table {
    private Direction buildingOrientation = Direction.EAST;
    private EventConsumer<TileDirectionChangedEvent> consumer;

    public static final Color BACKGROUND_COLOUR = new Color(0.2f, 0.2f, 0.2f, 1.0f);

    public BuildMenu() {
        ButtonGroup<Button> buttonGroup = new ButtonGroup<>();
        Texture defaultTexture = new Texture(Gdx.files.internal("textures/temp-building-sprite.png"));

        buttonGroup.add(new BuildButton(this, BuildingType.BELT_STRAIGHT));
        buttonGroup.add(new BuildButton(this, BuildingType.BELT_LEFT));
        buttonGroup.add(new BuildButton(this, BuildingType.BELT_RIGHT));
        buttonGroup.add(new BuildButton(this, BuildingType.SPLITTER));
        buttonGroup.add(new BuildButton(this, BuildingType.MERGER));
        buttonGroup.add(new ModeButton(defaultTexture, BuildingMode.COLOUR));
        buttonGroup.add(new ModeButton(defaultTexture, BuildingMode.ROTATE));
        buttonGroup.add(new ModeButton(defaultTexture, BuildingMode.DELETE));

        Label.LabelStyle style = new Label.LabelStyle(getFont(), Color.WHITE);
        add(new Label("Build menu:", style)).expandX();
        row();

        Table subTable = new Table();
        buttonGroup.getButtons().forEach(subTable::add);
        add(subTable);

        consumer = this::onBuildingDirectionChanged;
        EventQueue.getInstance().registerConsumer(consumer, EventType.TILE_DIRECTION_CHANGED_EVENT);
    }

    public static BitmapFont getFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FreeMono.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.size = 16;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose(); // don't forget to dispose to avoid memory leaks!
        return font;
    }

    public void dispose() {
        EventQueue.getInstance().deregisterConsumer(consumer, EventType.TILE_DIRECTION_CHANGED_EVENT);
    }

    public Direction getBuildingOrientation() {
        return buildingOrientation;
    }

    public void onBuildingDirectionChanged(TileDirectionChangedEvent tileDirectionChangedEvent) {
        if (tileDirectionChangedEvent.rotateRight()) {
            buildingOrientation = buildingOrientation.rotateRight();
        } else {
            buildingOrientation = buildingOrientation.rotateLeft();
        }
    }
}
