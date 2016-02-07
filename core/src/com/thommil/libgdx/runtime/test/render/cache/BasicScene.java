package com.thommil.libgdx.runtime.test.render.cache;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.scene.Scene;

/**
 * Test for a Simple Scene display :
 * 	- draw a static srite in middle of scene
 */
public class BasicScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_ERROR);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 4;
		settings.viewport.minWorldHeight = 4;
		Scene defaultScene = new Scene(settings);

		//Layers
		defaultScene.addLayer(0,new BasicLayer());

		//Actor
		defaultScene.addActor(new BasicActor());

		//Start
		this.setScreen(defaultScene);
	}
}
