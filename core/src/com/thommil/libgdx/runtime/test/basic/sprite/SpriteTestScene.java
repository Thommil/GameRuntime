package com.thommil.libgdx.runtime.test.basic.sprite;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Test for a Simple Sprite display
 */
public class SpriteTestScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 4;
		settings.viewport.minWorldHeight = 4;
		Scene scene = new Scene(settings);

		//Layers
		scene.addLayer(new SpriteBatchLayer(1));

		//Actor
		scene.addActor(new CuriosityActor());

		//Profiler
		SceneProfiler.profile(scene);

		//Start
		this.setScreen(scene);
	}
}
