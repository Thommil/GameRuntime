package com.thommil.libgdx.runtime.test.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Affine2;
import com.thommil.libgdx.runtime.scene.Actor;
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
		settings.viewport.minWorldWidth = 4;
		settings.viewport.minWorldHeight = 4;
		settings.renderer.layers = 1;
		Scene defaultScene = new Scene(settings);
		this.setScreen(defaultScene);

		//Layers
		defaultScene.setLayer(0,new BasicLayer());

		//Actor
		defaultScene.addActor(new SpriteActor());
	}
}
