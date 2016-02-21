package com.thommil.libgdx.runtime.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.thommil.libgdx.runtime.test.basic.cache.CacheTestScene;
import com.thommil.libgdx.runtime.test.basic.sprite.SpriteTestScene;
import com.thommil.libgdx.runtime.test.basic.texture.TextureTestScene;
import com.thommil.libgdx.runtime.test.input.dynamic.PhysicsInputTestScene;
import com.thommil.libgdx.runtime.test.input.kinematic.KinematicInputTestScene;
import com.thommil.libgdx.runtime.test.physics.basic.BasicPhysicsTestScene;
import com.thommil.libgdx.runtime.test.physics.collision.CollisionTestScene;
import com.thommil.libgdx.runtime.test.physics.softbody.SoftBodyTestScene;
import com.thommil.libgdx.runtime.test.physics.stress.PhysicsStressTestScene;
import com.thommil.libgdx.runtime.test.render.particles.ParticlesTestScene;
import com.thommil.libgdx.runtime.test.render.softbody.SoftBodyRenderTestScene;
import com.thommil.libgdx.runtime.test.render.water.WaterTestScene;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=600;
		config.height=600;
		//Basic texture
		//new LwjglApplication(new TextureTestScene(), config);

		//Basic sprite
		//new LwjglApplication(new SpriteTestScene(), config);

		//Basic cache
		//new LwjglApplication(new CacheTestScene(), config);

		//Basic Physics
		//new LwjglApplication(new BasicPhysicsTestScene(), config);

		//Stress Physics
		//new LwjglApplication(new PhysicsStressTestScene(), config);

		//Collision Physics
		//new LwjglApplication(new CollisionTestScene(), config);

		//Particles Sprite
		//new LwjglApplication(new ParticlesTestScene(), config);

		//Softbody Physics
		new LwjglApplication(new SoftBodyTestScene(), config);

		//Softbody Rendering
		//new LwjglApplication(new SoftBodyRenderTestScene(), config);

		//Kinematic input
		//new LwjglApplication(new KinematicInputTestScene(), config);

		//Physics input
		//new LwjglApplication(new PhysicsInputTestScene(), config);

		//Water rendering
		//new LwjglApplication(new WaterTestScene(), config);


	}
}
