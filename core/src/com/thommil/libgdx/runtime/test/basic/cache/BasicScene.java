package com.thommil.libgdx.runtime.test.basic.cache;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.BasicBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.BasicCacheLayer;

/**
 * Test for a Simple Sprite display
 */
public class BasicScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 10;
		settings.viewport.minWorldHeight = 10;
		//settings.physics.asyncMode=false;
		Scene defaultScene = new Scene(settings);

		//Layers
		defaultScene.addLayer(0,new BasicCacheLayer(6));

		//Actor
		Texture texture = new Texture(Gdx.files.internal("planet_sprite.png"));
		defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.5f,4f),0));
		defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.5f,4f),1));
		defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.5f,4f),2));
		defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.5f,4f),3));
		defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.5f,4f),4));
		defaultScene.addActor(new BasicActor(texture, MathUtils.random(0.5f,4f),5));

		//Start
		this.setScreen(defaultScene);
	}
}
