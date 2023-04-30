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
import com.lipsum.game.entities.Consumer;
import com.lipsum.game.entities.Producer;
import com.lipsum.game.event.EventQueue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lipsum.game.actions.MoveConveyor;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.entities.Packet;
import com.lipsum.game.factory.factories.BuildingFactory;
import com.lipsum.game.factory.factories.ConveyorFactory;
import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.states.PlayerState;
import com.lipsum.game.ui.hud.HudUI;
import com.lipsum.game.factory.factories.PacketFactory;
import com.lipsum.game.util.Direction;
import com.lipsum.game.util.PacketType;
import com.lipsum.game.world.World;

import java.util.List;
import java.util.Random;

public class LDJam53 extends ApplicationAdapter {
	World world;
	InputMultiplexer inputMultiplexer;
	PlayerState playerState;
	HudUI hudUI;

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
		hudUI = new HudUI();

		hudUI.create(inputMultiplexer);

		stage = new Stage(new ScreenViewport(camera));
		world = World.init(10, stage);
		stage.addActor(world);
		stage.addActor(machineGroup);
		stage.addActor(packetGroup);

		new Conveyor(3, 2, List.of(Direction.SOUTH, Direction.WEST, Direction.EAST), List.of(Direction.NORTH));
		new Conveyor(2, 2, List.of(Direction.SOUTH), List.of(Direction.EAST));
		new Conveyor(4, 2, List.of(Direction.SOUTH), List.of(Direction.WEST));
		new Conveyor(3, 3, Direction.NORTH);
		new Conveyor(3, 4, List.of(Direction.SOUTH), List.of(Direction.WEST, Direction.EAST));
		new Conveyor(2, 4, Direction.WEST);
		new Conveyor(4, 4, Direction.EAST);
		new Conveyor(5, 4, List.of(Direction.WEST), List.of(Direction.NORTH));
		new Conveyor(1, 4, List.of(Direction.EAST), List.of(Direction.NORTH));
		new Conveyor(5, 5, Direction.NORTH);
		new Conveyor(1, 5, Direction.NORTH);
		new Producer(3, 1, Direction.NORTH, PacketType.BLUE);
		new Producer(2, 1, Direction.NORTH, PacketType.RED);
		new Producer(4, 1, Direction.NORTH, PacketType.YELLOW);
		new Consumer(1,6, Direction.NORTH, PacketType.YELLOW);
		new Consumer(5,6, Direction.NORTH, PacketType.BLUE);
		new Consumer(3,6, Direction.NORTH, PacketType.RED);

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