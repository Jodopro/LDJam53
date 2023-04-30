package com.lipsum.game.ui.hud;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.event.events.SelectedModeChangedEvent;
import com.lipsum.game.managers.building.catalog.BuildingMode;
import com.lipsum.game.util.PacketType;

import java.util.stream.Stream;

public class ModeButton extends Button {

    private final BuildingMode type;
    private final ButtonClickedListener listener;
    private PacketType packetType = PacketType.RED;

    public ModeButton(Texture texture, BuildingMode type) {
        super(new ButtonStyle(
                new ModeButtonDrawable(type, ButtonState.DEFAULT),
                new ModeButtonDrawable(type, ButtonState.PRESSED),
                new ModeButtonDrawable(type, ButtonState.SELECTED)
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
            if (button == Input.Buttons.LEFT) {
                EventQueue.getInstance().invoke(new SelectedModeChangedEvent(type, packetType));
                return true;
            } else if (button == Input.Buttons.RIGHT && type == BuildingMode.COLOUR) {
                packetType = PacketType.values()[(packetType.ordinal() + 1) % PacketType.values().length];
                updateDrawables();
                EventQueue.getInstance().invoke(new SelectedModeChangedEvent(type, packetType));
                return true;
            }
            return false;
        }
    }

    private void updateDrawables() {
        Stream.of(getStyle().checked, getStyle().up, getStyle().down)
                .filter(d -> d instanceof ModeButtonDrawable)
                .map(d -> (ModeButtonDrawable) d)
                .forEach(d -> d.setPacketType(packetType));
    }
}
