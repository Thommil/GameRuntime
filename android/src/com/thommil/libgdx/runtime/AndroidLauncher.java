package com.thommil.libgdx.runtime;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.thommil.libgdx.runtime.test.test_01_screens.ScreensGame;
import com.thommil.libgdx.runtime.test.test_02_spritecache.SpriteCacheGame;
import com.thommil.libgdx.runtime.test.test_03_spritebatch.SpriteBatchGame;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		Game game;

		// TEST 01 - Screens Workflow
		game = new ScreensGame();

		// TEST 02 - Sprite Cache
		//game = new SpriteCacheGame();

		// TEST 03 - Sprite Batch
		game = new SpriteBatchGame();

		initialize(game, config);

	}
}
