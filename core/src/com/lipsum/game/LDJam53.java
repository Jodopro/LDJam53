package com.lipsum.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.lipsum.game.factory.EntityFactory;
import com.lipsum.game.rendering.EntityRenderer;

public class LDJam53 extends ApplicationAdapter {

	
	@Override
	public void create () {

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		EntityRenderer.getInstance().draw();
	}
	
	@Override
	public void dispose () {
		EntityFactory.getInstance().removeManagedObjects();
	}
}
