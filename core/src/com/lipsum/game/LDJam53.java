package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lipsum.game.event.EventQueue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lipsum.game.actions.MoveConveyor;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.entities.Packet;
import com.lipsum.game.factory.factories.BuildingFactory;
import com.lipsum.game.factory.factories.ConveyorFactory;
import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.factory.factories.PacketFactory;

import java.util.List;
import java.util.Random;

public class LDJam53 extends ApplicationAdapter {
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
		stage = new Stage(new ScreenViewport());
		stage.addActor(machineGroup);
		stage.addActor(packetGroup);

		Conveyor c1 = new Conveyor(2, 1, Conveyor.Direction.NORTH);
		new Conveyor(2, 2, Conveyor.Direction.NORTH);
//		new Conveyor(2, 3, List.of(Conveyor.Direction.SOUTH), List.of(Conveyor.Direction.WEST, Conveyor.Direction.EAST));
//		new Conveyor(2, 3, List.of(Conveyor.Direction.SOUTH), List.of(Conveyor.Direction.EAST));
		new Conveyor(2, 3, List.of(Conveyor.Direction.SOUTH), List.of(Conveyor.Direction.WEST));
		new Conveyor(1, 3, Conveyor.Direction.WEST);
		new Conveyor(3, 3, Conveyor.Direction.EAST);
		new Conveyor(4, 3, List.of(Conveyor.Direction.WEST), List.of(Conveyor.Direction.NORTH));
		new Conveyor(0, 3, List.of(Conveyor.Direction.EAST), List.of(Conveyor.Direction.NORTH));
		new Conveyor(4, 4, Conveyor.Direction.NORTH);
		new Conveyor(0, 4, Conveyor.Direction.NORTH);

		Packet p = new Packet();
		c1.setPacket(p);
		c1.setCurrentFrom(Conveyor.Direction.SOUTH);
		c1.setCurrentTo(Conveyor.Direction.NORTH);
		MoveConveyor moveConveyor = new MoveConveyor(c1, 1);
		c1.addAction(moveConveyor);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void resize (int width, int height) {
		// See below for what true means.
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		EventQueue.getInstance().handleAll();

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