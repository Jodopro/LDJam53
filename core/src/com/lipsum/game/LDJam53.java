package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lipsum.game.entity.entities.Blep;
import com.lipsum.game.entity.entities.Blip;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.factory.EntityFactory;
import com.lipsum.game.factory.factories.BlepFactory;
import com.lipsum.game.factory.factories.BlipFactory;
import com.lipsum.game.rendering.EntityRenderer;

import java.util.Random;

public class LDJam53 extends ApplicationAdapter {

	
	@Override
	public void create () {
		BlipFactory.getInstance();
		BlepFactory.getInstance();

		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			new Blip(random.nextFloat(100, 400), random.nextFloat(100, 400));
		}
		for (int i = 0; i < 500; i++) {
			new Blep(random.nextFloat(100, 400), random.nextFloat(100, 400));
		}
	}

	@Override
	public void render () {
		EventQueue.getInstance().handleAll();
		// System.out.println(BlipFactory.getInstance().getAllManagedObjects().toList());

		EntityRenderer.getInstance().act(Gdx.graphics.getDeltaTime());

		ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1.0f);
		EntityRenderer.getInstance().draw();
	}
	
	@Override
	public void dispose () {
		EntityFactory.getInstance().removeManagedObjects();
	}
}
