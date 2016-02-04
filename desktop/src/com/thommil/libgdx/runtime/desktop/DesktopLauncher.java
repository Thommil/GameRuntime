package com.thommil.libgdx.runtime.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.thommil.libgdx.runtime.test.basic.BasicScene;
import com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=600;
		config.height=600;
		new LwjglApplication(new PhysicsScene(), config);
	}
}
