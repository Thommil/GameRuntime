package com.thommil.libgdx.runtime.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=800;
		config.height=600;
		//Basic
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.basic.BasicScene(), config);
		//Basic Physics
		new LwjglApplication(new com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene(), config);
		//Basic stress Physics
		//new LwjglApplication(new com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene(), config);
	}
}
