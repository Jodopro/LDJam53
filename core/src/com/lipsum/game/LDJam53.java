package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lipsum.game.actors.Layer;
import com.lipsum.game.actors.MyActor;
import com.lipsum.game.actors.World;

public class LDJam53 extends ApplicationAdapter {

	private Stage stage;
	
	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		World w = new World();
		stage.addActor(w);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize (int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		ScreenUtils.clear(1, 0, 0, 1);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose () {
		stage.dispose();
	}
}