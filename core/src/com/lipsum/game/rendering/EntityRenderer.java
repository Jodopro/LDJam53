package com.lipsum.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EntityRenderer {
    private static final EntityRenderer instance = new EntityRenderer();
    private final Stage stage;
    private final Viewport viewPort;
    private final Camera camera;

    public static EntityRenderer getInstance() {
        return instance;
    }

    public EntityRenderer() {
        camera = new OrthographicCamera();
        viewPort = new ScreenViewport(camera);
        stage = new Stage(viewPort);
    }

    public void act(float dt) {
        stage.act(dt);
    }

    public void draw() {
        viewPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getViewport().apply();
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }
}
