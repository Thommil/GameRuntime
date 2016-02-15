package com.thommil.libgdx.runtime.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=600;
		config.height=600;
		//Basic texture
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.basic.texture.BasicScene(), config);
		//Basic sprite
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.basic.sprite.BasicScene(), config);
		//Basic cache
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.basic.cache.BasicScene(), config);

		//Basic Physics
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene(), config);
		//Stress Physics
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene(), config);
		//Collision Physics
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.physics.collision.PhysicsScene(), config);
		//Particles Physics
		new LwjglApplication(new com.thommil.libgdx.runtime.test.physics.particles.PhysicsScene(), config);

		//Kinematic input
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.input.kinematic.BasicScene(), config);
		//Physics input
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.input.dynamic.PhysicsScene(), config);

	}
}
