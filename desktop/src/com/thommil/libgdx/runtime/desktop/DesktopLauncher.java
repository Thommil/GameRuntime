package com.thommil.libgdx.runtime.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=800;
		config.height=600;
		//Basic texture
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.basic.texture.BasicScene(), config);
		//Basic sprite
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.basic.sprite.BasicScene(), config);

		//Basic Physics
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene(), config);
		//Basic stress Physics
		new LwjglApplication(new com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene(), config);

		//Kinematic input
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.input.kinematic.BasicScene(), config);
		//Physics input
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.input.dynamic.PhysicsScene(), config);

		//Render cache
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.render.cache.BasicScene(), config);
	}
}
