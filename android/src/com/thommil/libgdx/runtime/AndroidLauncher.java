package com.thommil.libgdx.runtime;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//Basic texture
		//initialize(new com.thommil.libgdx.runtime.test.basic.texture.BasicScene(), config);
		//Basic sprite
		//initialize(new com.thommil.libgdx.runtime.test.basic.sprite.BasicScene(), config);

		//Basic Physics
		//initialize(new com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene(), config);
		//Basic stress Physics
		//initialize(new com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene(), config);

		//Kinematic input
		//initialize(new com.thommil.libgdx.runtime.test.input.kinematic.BasicScene(), config);
		//Physics input
		//initialize(new com.thommil.libgdx.runtime.test.input.dynamic.PhysicsScene(), config);

		//Render cache
		//initialize(new com.thommil.libgdx.runtime.test.render.cache.BasicScene(), config);
	}
}
