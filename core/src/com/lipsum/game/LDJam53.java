package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lipsum.game.event.EventQueue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.entities.Packet;
import com.lipsum.game.factory.factories.BuildingFactory;
import com.lipsum.game.factory.factories.ConveyorFactory;
import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.ui.hud.HudUI;
import com.lipsum.game.factory.factories.PacketFactory;
import com.lipsum.game.world.World;

public class LDJam53 extends ApplicationAdapter {
	SpriteBatch batch;
	World world;
	private OrthographicCamera camera;

	InputMultiplexer inputMultiplexer;
	HudUI hudUI = new HudUI();

	Stage stage;
	public static Group packetGroup = new Group();
	public static Group machineGroup = new Group();
	static {
		// Init all factories here, since static blocks are only executed when the class is used.
		// Missing factories here will potentially make them invisible to super-factories
		EntityFactory.getInstance();
		BuildingFactory.getInstance();
		ConveyorFactory.getInstance();
		PacketFactory.getInstance();
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
		inputMultiplexer = new InputMultiplexer();

		stage = new Stage(new ScreenViewport());
		stage.addActor(machineGroup);
		stage.addActor(packetGroup);

		Conveyor c1 = new Conveyor(1, 1, Conveyor.Direction.NORTH);
		Conveyor c2 = new Conveyor(1, 2, Conveyor.Direction.NORTH);
		Conveyor c3 = new Conveyor(1, 3, Conveyor.Direction.EAST);
		Packet p = new Packet();
		c1.addPacket(p);
		inputMultiplexer.addProcessor(stage);

		hudUI.create(inputMultiplexer);

		Gdx.input.setInputProcessor(inputMultiplexer);

	}

	@Override
	public void resize (int width, int height) {
		super.resize(width, height);
		hudUI.resize(width, height);

		// See below for what true means.
		stage.getViewport().update(width, height, true);
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

		float delta = Gdx.graphics.getDeltaTime();
		stage.act(delta);
		stage.draw();

		hudUI.render();
	}
	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		stage.dispose();
		hudUI.dispose();
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