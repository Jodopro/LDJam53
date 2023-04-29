package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lipsum.game.event.EventQueue;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lipsum.game.actions.MoveConveyor;
import com.lipsum.game.entities.Conveyor;
import com.lipsum.game.entities.Packet;
import com.lipsum.game.factory.factories.EntityFactory;

public class LDJam53 extends ApplicationAdapter {
	Stage stage;
	static {
		// Init all factories here, since static blocks are only executed when the class is used.
		// Missing factories here will potentially make them invisible to super-factories
		EntityFactory.getInstance();
	}
	
	@Override
	public void create () {
		stage = new Stage(new ScreenViewport());
		Group packetGroup = new Group();
		Group machineGroup = new Group();
		stage.addActor(machineGroup);
		stage.addActor(packetGroup);

		Conveyor c = new Conveyor(10, 10, "north");
		machineGroup.addActor(c);
		Packet p = new Packet();
		c.addPacket(p);
		packetGroup.addActor(p);
		c.addAction(new MoveConveyor(c, 0.5f));
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