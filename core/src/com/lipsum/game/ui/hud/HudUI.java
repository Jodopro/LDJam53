package com.lipsum.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;
import com.lipsum.game.states.PlayerState;
import com.lipsum.game.ui.BaseUI;

/**
 * Renders the in-game HUD for building etc
 */
public class HudUI extends BaseUI {
    private PlayerState playerState;
    private BitmapFont bitmapFont;

    @Override
    public void create(InputMultiplexer inputMultiplexer) {
        super.create(inputMultiplexer);
        table.align(Align.bottomLeft);
        table.add(new BuildMenu());
        stage.addActor(table);
        playerState = PlayerState.getInstance();
        bitmapFont = new BitmapFont();
    }

    public void render () {
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();


        float x = Gdx.graphics.getWidth() - 200;
        float y = 50;

        stage.getBatch().begin();
        bitmapFont.draw(stage.getBatch(), String.format("Money: %f", playerState.getMoneyLeft()), x, y);
        y -= 20;
        bitmapFont.draw(stage.getBatch(), String.format("Score: %f", playerState.getScore()), x, y);
        y -= 20;
        bitmapFont.draw(stage.getBatch(), String.format("Health: %f", playerState.getHealth()), x, y);
        stage.getBatch().end();
    }

    public void dispose() {
        stage.dispose();
    }
}
