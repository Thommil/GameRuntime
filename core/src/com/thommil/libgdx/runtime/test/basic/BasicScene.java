package com.thommil.libgdx.runtime.test.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Affine2;
import com.thommil.libgdx.runtime.Scene;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.Renderable;

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
		settings.viewport.minWorldWidth = 10;
		settings.viewport.minWorldHeight = 5;
		settings.renderer.maxLayers = 1;
		Scene defaultScene = new Scene(settings);
		this.setScreen(defaultScene);

		//Actor
		final Affine2 affine = new Affine2();

		SpriteActor actor = new SpriteActor(affine);
		defaultScene.addActor(actor);
	}
}
