package com.thommil.libgdx.runtime;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.thommil.libgdx.runtime.test.test_01_screens.ScreensGame;
import com.thommil.libgdx.runtime.test.test_02_spritecache.SpriteCacheGame;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		Game game;

		// TEST 01 - Screens Workflow
		game = new ScreensGame();

		// TEST 02 - Screens Workflow
		//game = new SpriteCacheGame();

		initialize(game, config);

	}
}
