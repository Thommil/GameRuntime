package com.thommil.libgdx.runtime;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.thommil.libgdx.runtime.test.test_01_screens.ScreensGame;
import com.thommil.libgdx.runtime.test.test_02_spritecache.SpriteCacheGame;
import com.thommil.libgdx.runtime.test.test_03_spritebatch.SpriteBatchGame;
import com.thommil.libgdx.runtime.test.test_04_rigidbody.RigidbodyGame;
import com.thommil.libgdx.runtime.test.test_05_softbody.SoftbodyGame;
import com.thommil.libgdx.runtime.test.test_06_input.InputGame;
import com.thommil.libgdx.runtime.test.test_07_particleseffect.ParticlesEffectGame;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		Game game;

		// TEST 01 - Screens Workflow
		//game = new ScreensGame();

		// TEST 02 - Sprite Cache
		//game = new SpriteCacheGame();

		// TEST 03 - Sprite Batch
		//game = new SpriteBatchGame();

		// TEST 04 - RigidBody
		//game = new RigidbodyGame();

		// TEST 05 - SoftBody
		//game= new SoftbodyGame();

		// TEST 06 - Input
		//game= new InputGame();

		// TEST 07 - Particles effect
		game= new ParticlesEffectGame();


		initialize(game, config);

	}
}
