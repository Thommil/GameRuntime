package com.thommil.libgdx.runtime.test.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Affine2;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Scene;

/**
 * Test for a Simple Scene display :
 * 	- draw a static srite in middle of scene
 */
public class BasicScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 2;
		settings.viewport.minWorldHeight = 2;

		Scene defaultScene = new Scene(settings);
		this.setScreen(defaultScene);

		//Layers
		defaultScene.addLayer(new BasicLayer());

		//Actor
		Affine2 affine = new Affine2();
		affine.translate(-0.65f,-0.57f);

		defaultScene.addActor(new TextureActor(affine));

	}
}
