package com.lipsum.game.rendering;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.lipsum.game.factory.EntityFactory;

public class EntityRenderer {
    private static final EntityRenderer instance = new EntityRenderer();
    private final Stage stage;
    private final ExtendViewport viewPort;

    public static EntityRenderer getInstance() {
        return instance;
    }

    public EntityRenderer() {
        viewPort = new ExtendViewport(500, 500);
        stage = new Stage();
    }

    public void draw() {
        stage.getViewport().apply();
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }
}
