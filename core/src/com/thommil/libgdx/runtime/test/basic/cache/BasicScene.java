package com.thommil.libgdx.runtime.test.basic.cache;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Test for a Simple Sprite display
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
		defaultScene.addLayer(0,new SpriteCacheLayer(60));

		//Actor
		Texture texture = new Texture(Gdx.files.internal("planet_sprite.png"));
		for(int i = 0; i < 10; i++) {
			defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.1f, 2f), 0));
			defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.1f, 2f), 1));
			defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.1f, 2f), 2));
			defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.1f, 2f), 3));
			defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.1f, 2f), 4));
			defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.1f, 2f), 5));
		}

		//Profiler
		SceneProfiler.profile(defaultScene);

		//Start
		this.setScreen(defaultScene);
	}
}
