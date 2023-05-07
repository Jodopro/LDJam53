package com.lipsum.game.ui.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.lipsum.game.states.PlayerState;
import com.lipsum.game.ui.BaseUI;
import com.lipsum.game.ui.styles.DefaultLabelStyle;

/**
 * Renders the in-game HUD for building etc
 */
public class HudUI extends BaseUI {
    private PlayerState playerState;
    private BuildMenu buildMenu;
    private Label moneyLabel;
    private Label healthLabel;
    private Label scoreLabel;
    private VerticalGroup statsGroup;

    @Override
    public void create(InputMultiplexer inputMultiplexer) {
        super.create(inputMultiplexer);
        table.align(Align.bottomLeft);
        buildMenu = new BuildMenu();
        table.add(buildMenu);
        stage.addActor(table);
        playerState = PlayerState.getInstance();
        Texture t = new Texture("background_tile3.png");
        statsGroup = new VerticalGroup(){
            @Override
            public void draw (Batch batch, float parentAlpha) {
                validate();
                batch.draw(t, getX(), getY(), this.getWidth(), this.getHeight());
                super.draw(batch, parentAlpha);
            }
        };
        int space = 5;
        int pad = 5;
        statsGroup.space(space);
        statsGroup.pad(pad);

        moneyLabel = new Label("String 1", DefaultLabelStyle.getLabelStyle());
        healthLabel = new Label("String 2", DefaultLabelStyle.getLabelStyle());
        scoreLabel = new Label("String 3", DefaultLabelStyle.getLabelStyle());

        statsGroup.addActor(moneyLabel);
        statsGroup.addActor(healthLabel);
        statsGroup.addActor(scoreLabel);

        statsGroup.align(Align.topLeft);
        stage.addActor(statsGroup);
    }

    public void render () {
        stage.act(Gdx.graphics.getDeltaTime());

        moneyLabel.setText(String.format("Money: %d", (int) playerState.getMoneyLeft()));
        healthLabel.setText(String.format("Health: %d", (int) playerState.getHealth()));
        scoreLabel.setText(String.format("Score: %d", (int) playerState.getScore()));
        statsGroup.setSize(statsGroup.getPrefWidth(), statsGroup.getPrefHeight());
        statsGroup.setY(stage.getHeight()-statsGroup.getHeight());

        stage.draw();
    }

    public void dispose() {
        buildMenu.dispose();
        stage.dispose();
    }
}
