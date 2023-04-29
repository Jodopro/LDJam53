package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.factory.factories.EntityFactory;

public class LDJam53 extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	static {
		// Init all factories here, since static blocks are only executed when the class is used.
		// Missing factories here will potentially make them invisible to super-factories
		EntityFactory.getInstance();
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		EventQueue.getInstance().handleAll();

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
