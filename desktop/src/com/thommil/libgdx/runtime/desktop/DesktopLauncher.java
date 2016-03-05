package com.thommil.libgdx.runtime.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.test.test_01_screens.ScreensGame;
import com.thommil.libgdx.runtime.test.test_02_spritecache.SpriteCacheGame;
import com.thommil.libgdx.runtime.test.test_03_spritebatch.SpriteBatchGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=600;
		config.height=600;

		Game game;

		// TEST 01 - Screens Workflow
		//game = new ScreensGame();

		// TEST 02 - Sprite Cache
		//game = new SpriteCacheGame();

		// TEST 03 - Sprite Batch
		game = new SpriteBatchGame();

		new LwjglApplication(game, config);
	}
}
