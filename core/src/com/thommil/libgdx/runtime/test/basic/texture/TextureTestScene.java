package com.thommil.libgdx.runtime.test.basic.texture;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Test for a Simple texture display
 */
public class TextureTestScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 10;
		settings.viewport.minWorldHeight = 10;
		Scene scene = new Scene(settings);

		//Layers
		scene.addLayer(new SpriteBatchLayer(1000));

		//Actor
		scene.addActor(new TextureActor());

		//Profiler
		SceneProfiler.profile(scene, SceneProfiler.ALL, 5000);

		//Start
		this.setScreen(scene);
	}
}
