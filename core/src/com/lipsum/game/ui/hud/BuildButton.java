package com.lipsum.game.ui.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.events.SelectedBuildingTypeChangedEvent;
import com.lipsum.game.event.events.SelectedModeChangedEvent;
import com.lipsum.game.managers.building.catalog.BuildingCatalog;
import com.lipsum.game.managers.building.catalog.BuildingMode;
import com.lipsum.game.managers.building.catalog.BuildingType;

import static com.lipsum.game.ui.hud.BuildMenu.getFont;

public class BuildButton extends Button {

    private final Texture texture;
    private final BuildingType type;
    private final ButtonClickedListener listener;

    public BuildButton(Texture texture, BuildingType type) {
        super(new ButtonStyle(
                new SubtitledImageDrawable(texture, type + "\n" + BuildingCatalog.getCost(type),
                        getFont(), ButtonState.DEFAULT),
                new SubtitledImageDrawable(texture, type + "\n" + BuildingCatalog.getCost(type),
                        getFont(), ButtonState.PRESSED),
                new SubtitledImageDrawable(texture, type + "\n" + BuildingCatalog.getCost(type),
                        getFont(), ButtonState.SELECTED)
        ));

        this.type = type;
        this.texture = texture;
        listener = new ButtonClickedListener();
        addListener(listener);
    }

    @Override
    public float getPrefWidth() {
        if (texture == null) {
            return 0;
        }
        return texture.getWidth() * 6;
    }

    @Override
    public float getPrefHeight() {
        if (texture == null) {
            return 0;
        }
        return texture.getHeight() * 6 + getFont().getLineHeight() * 2;
    }

    @Override
    public boolean remove() {
        removeListener(listener);
        return super.remove();
    }

    class ButtonClickedListener extends ClickListener {
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            EventQueue.getInstance().invoke(new SelectedModeChangedEvent(BuildingMode.BUILDING));
            EventQueue.getInstance().invoke(new SelectedBuildingTypeChangedEvent(type));
            return true;
        }
    }
}
