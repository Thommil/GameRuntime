package com.thommil.libgdx.runtime.test.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.Scene;

/**
 * Test for a Simple Scene display :
 * 	- ...
 */
public class BasicScene extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 10;
		settings.viewport.minWorldHeight = 5;
		Scene defaultScene = new Scene(settings);
		this.setScreen(defaultScene);
	}
}
