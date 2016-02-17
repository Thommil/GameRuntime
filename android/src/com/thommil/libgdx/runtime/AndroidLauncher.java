package com.thommil.libgdx.runtime;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.thommil.libgdx.runtime.test.basic.cache.CacheTestScene;
import com.thommil.libgdx.runtime.test.basic.sprite.SpriteTestScene;
import com.thommil.libgdx.runtime.test.basic.texture.TextureTestScene;
import com.thommil.libgdx.runtime.test.input.dynamic.PhysicsInputTestScene;
import com.thommil.libgdx.runtime.test.input.kinematic.KinematicInputTestScene;
import com.thommil.libgdx.runtime.test.physics.basic.BasicPhysicsTestScene;
import com.thommil.libgdx.runtime.test.physics.collision.CollisionTestScene;
import com.thommil.libgdx.runtime.test.physics.particles.SoftBodyTestScene;
import com.thommil.libgdx.runtime.test.physics.stress.PhysicsStressTestScene;
import com.thommil.libgdx.runtime.test.render.water.WaterTestScene;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//Basic texture
		//initialize(new TextureTestScene(), config);
		//Basic sprite
		//initialize(new SpriteTestScene(), config);
		//Basic cache
		//initialize(new CacheTestScene(), config);

		//Basic Physics
		//initialize(new BasicPhysicsTestScene(), config);
		//Stress Physics
		//initialize(new PhysicsStressTestScene(), config);
		//Collision Physics
		//initialize(new CollisionTestScene(), config);
		//Particles Physics
		//initialize(new SoftBodyTestScene(), config);

		//Kinematic input
		//initialize(new KinematicInputTestScene(), config);
		//Physics input
		//initialize(new PhysicsInputTestScene(), config);

		//Water rendering
		initialize(new WaterTestScene(), config);


	}
}
