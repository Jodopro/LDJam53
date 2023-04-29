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
	World world;
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
		var camera = new OrthographicCamera(30f, 30f);
		inputMultiplexer = new InputMultiplexer();

		hudUI.create(inputMultiplexer);

		stage = new Stage(new ScreenViewport(camera));
		world = new World(10, stage);

		stage.addActor(world);
		stage.addActor(machineGroup);
		stage.addActor(packetGroup);

		Conveyor c1 = new Conveyor(1, 1, Conveyor.Direction.NORTH);
		Conveyor c2 = new Conveyor(1, 2, Conveyor.Direction.NORTH);
		Conveyor c3 = new Conveyor(1, 3, Conveyor.Direction.EAST);
		Packet p = new Packet();
		c1.addPacket(p);
		inputMultiplexer.addProcessor(stage);


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
		world.step();

		ScreenUtils.clear(0, 0, 0, 1);
		float delta = Gdx.graphics.getDeltaTime();
		stage.act(delta);
		stage.draw();

		hudUI.render();
	}
	@Override
	public void dispose () {
		world.dispose();
		stage.dispose();
		hudUI.dispose();
	}
}