package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.world.World;

public class LDJam53 extends ApplicationAdapter {
	SpriteBatch batch;
	World world;
	private OrthographicCamera camera;

	static {
		// Init all factories here, since static blocks are only executed when the class is used.
		// Missing factories here will potentially make them invisible to super-factories
		EntityFactory.getInstance();
	}
	
	@Override
	public void create () {
		camera = new OrthographicCamera(30f, 30f);
		world = new World(10, camera);


		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.zoom = 10f;
		camera.update();

		Gdx.graphics.setWindowedMode(800, 800);

		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		EventQueue.getInstance().handleAll();

		handleInput();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		world.step();

		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();
		world.draw(batch, 1f);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.zoom += 0.2;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			camera.zoom -= 0.2;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-9, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(9, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -9, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, 9, 0);
		}

		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 1000/camera.viewportWidth);
	}
}
