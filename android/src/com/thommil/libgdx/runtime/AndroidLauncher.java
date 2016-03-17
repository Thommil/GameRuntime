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
import com.thommil.libgdx.runtime.test.test_08_liquid.LiquidGame;
import com.thommil.libgdx.runtime.test.test_09_normalmap.NormalMapGame;
import com.thommil.libgdx.runtime.test.test_10_fine_offscreen.FineOffscreenGame;
import com.thommil.libgdx.runtime.test.test_11_events.EventsGame;
import com.thommil.libgdx.runtime.test.test_12_layout.LayoutGame;

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
		//game= new ParticlesEffectGame();

		// TEST 08 - Liquid
		//game= new LiquidGame();

		// TEST 09 - NormalMap
		//game= new NormalMapGame();

		// TEST 10 - Fine Offscreen rendering
		//game= new FineOffscreenGame();

		// TEST 11 - Events touch dispatcher
		//game= new EventsGame();

		// TEST 12 - Layout
		game= new LayoutGame();

		initialize(game, config);

	}
}
