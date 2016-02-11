package com.thommil.libgdx.runtime.test.render.cache;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.BasicSpriteLayer;

/**
 * Test cache system for rendering
 */
public class BasicScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 20;
		settings.viewport.minWorldHeight = 20;
		Scene defaultScene = new Scene(settings);

		//Layers
		defaultScene.addLayer(0,new CacheLayer());

		//Start
		this.setScreen(defaultScene);
	}
}
