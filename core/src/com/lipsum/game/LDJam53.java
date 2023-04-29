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
import com.lipsum.game.entities.Producer;
import com.lipsum.game.event.EventQueue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lipsum.game.actions.MoveConveyor;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.entities.Packet;
import com.lipsum.game.factory.factories.BuildingFactory;
import com.lipsum.game.factory.factories.ConveyorFactory;
import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.ui.hud.HudUI;
import com.lipsum.game.factory.factories.PacketFactory;
import com.lipsum.game.world.World;

import java.util.List;
import java.util.Random;

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
		world = new World(10, camera);
		inputMultiplexer = new InputMultiplexer();

		stage = new Stage(new ScreenViewport(camera));
		stage.addActor(world);
		stage.addActor(machineGroup);
		stage.addActor(packetGroup);

		Conveyor c1 = new Conveyor(2, 1, Conveyor.Direction.NORTH);
		new Conveyor(2, 2, Conveyor.Direction.NORTH);
		new Conveyor(2, 3, List.of(Conveyor.Direction.SOUTH), List.of(Conveyor.Direction.WEST, Conveyor.Direction.EAST));
		new Conveyor(1, 3, Conveyor.Direction.WEST);
		new Conveyor(3, 3, Conveyor.Direction.EAST);
		new Conveyor(4, 3, List.of(Conveyor.Direction.WEST), List.of(Conveyor.Direction.NORTH));
		new Conveyor(0, 3, List.of(Conveyor.Direction.EAST), List.of(Conveyor.Direction.NORTH));
		new Conveyor(4, 4, Conveyor.Direction.NORTH);
		new Conveyor(0, 4, Conveyor.Direction.NORTH);
		new Producer(2, 0, Conveyor.Direction.NORTH);

		Packet p = new Packet();
		c1.setPacket(p);
		c1.setCurrentFrom(Conveyor.Direction.SOUTH);
		c1.setCurrentTo(Conveyor.Direction.NORTH);
		MoveConveyor moveConveyor = new MoveConveyor(c1, 1);
		c1.addAction(moveConveyor);

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