package com.thommil.libgdx.runtime.test.basic.cache;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.CacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Test for a Simple Sprite display
 */
public class CacheTestScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 10;
		settings.viewport.minWorldHeight = 10;
		Scene scene = new Scene(settings);

		//Layers
		CacheLayer.setSize(5000);
		CacheLayer planetCacheLayer = new CacheLayer(5000);
		scene.addLayer(planetCacheLayer);

		//Actor
		Texture texture = new Texture(Gdx.files.internal("planet_sprite.png"));
		planetCacheLayer.beginCache();
		for(int i = 0; i < 800; i++) {
			scene.addActor(new PlanetActor(texture, MathUtils.random(0.1f, 2f), 0));
			scene.addActor(new PlanetActor(texture, MathUtils.random(0.1f, 2f), 1));
			scene.addActor(new PlanetActor(texture, MathUtils.random(0.1f, 2f), 2));
			scene.addActor(new PlanetActor(texture, MathUtils.random(0.1f, 2f), 3));
			scene.addActor(new PlanetActor(texture, MathUtils.random(0.1f, 2f), 4));
			scene.addActor(new PlanetActor(texture, MathUtils.random(0.1f, 2f), 5));
		}
		planetCacheLayer.endCache();

		//Profiler
		SceneProfiler.profile(scene);

		//Start
		this.setScreen(scene);
	}
}
