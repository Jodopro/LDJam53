package com.lipsum.game.ui.hud;

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

    private final BuildingType type;
    private final ButtonClickedListener listener;

    public BuildButton(BuildMenu buildMenu, BuildingType type) {
        super(new ButtonStyle(
                new SubtitledImageDrawable(buildMenu, type,
                        getFont(), ButtonState.DEFAULT),
                new SubtitledImageDrawable(buildMenu, type,
                        getFont(), ButtonState.PRESSED),
                new SubtitledImageDrawable(buildMenu, type,
                        getFont(), ButtonState.SELECTED)
        ));

        this.type = type;
        listener = new ButtonClickedListener();
        addListener(listener);
    }

    @Override
    public float getPrefWidth() {
        return BuildButtonHelper.SIZE * BuildButtonHelper.SCALE;
    }

    @Override
    public float getPrefHeight() {
        return BuildButtonHelper.SIZE * BuildButtonHelper.SCALE;
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
