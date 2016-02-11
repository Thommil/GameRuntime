package com.thommil.libgdx.runtime.test.basic.texture;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.BasicSpriteLayer;

/**
 * Test for a Simple texture display
 */
public class BasicScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 10;
		settings.viewport.minWorldHeight = 10;
		Scene defaultScene = new Scene(settings);

		//Layers
		defaultScene.addLayer(0,new BasicSpriteLayer(1000));

		//Actor
		defaultScene.addActor(new BasicActor());

		//Start
		this.setScreen(defaultScene);
	}
}
