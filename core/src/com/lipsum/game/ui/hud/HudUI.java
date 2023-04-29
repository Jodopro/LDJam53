package com.lipsum.game.ui.hud;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.Align;
import com.lipsum.game.ui.BaseUI;

/**
 * Renders the in-game HUD for building etc
 */
public class HudUI extends BaseUI {
    @Override
    public void create(InputMultiplexer inputMultiplexer) {
        super.create(inputMultiplexer);
        table.align(Align.bottomLeft);
        table.add(new BuildMenu());
        stage.addActor(table);
    }
}
