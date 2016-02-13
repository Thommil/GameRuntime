package com.thommil.libgdx.runtime;

import android.os.Bundle;
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
		//Basic cache
		//initialize(new com.thommil.libgdx.runtime.test.basic.cache.BasicScene(), config);

		//Basic Physics
		//initialize(new com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene(), config);
		//Stress Physics
		//initialize(new com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene(), config);
		//Collision Physics
		initialize(new com.thommil.libgdx.runtime.test.physics.collision.PhysicsScene(), config);

		//Kinematic input
		//initialize(new com.thommil.libgdx.runtime.test.input.kinematic.BasicScene(), config);
		//Physics input
		//initialize(new com.thommil.libgdx.runtime.test.input.dynamic.PhysicsScene(), config);
	}
}
