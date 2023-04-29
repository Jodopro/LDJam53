package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lipsum.game.event.EventQueue;
import com.lipsum.game.factory.factories.EntityFactory;
import com.lipsum.game.ui.hud.HudUI;

public class LDJam53 extends ApplicationAdapter {
	HudUI hudUI = new HudUI();

	static {
		// Init all factories here, since static blocks are only executed when the class is used.
		// Missing factories here will potentially make them invisible to super-factories
		EntityFactory.getInstance();
	}
	
	@Override
	public void create () {
		hudUI.create();
	}

	@Override
	public void render () {
		EventQueue.getInstance().handleAll();

		ScreenUtils.clear(0, 0, 0, 1);

		hudUI.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		hudUI.resize(width, height);
	}

	@Override
	public void dispose () {
		hudUI.dispose();
	}
}
