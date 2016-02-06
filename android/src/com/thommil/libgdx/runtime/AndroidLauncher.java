package com.thommil.libgdx.runtime;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//Basic
		//initialize(new com.thommil.libgdx.runtime.test.basic.BasicScene(), config);
		//Basic Physics
		initialize(new com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene(), config);
		//Basic stress Physics
		//initialize(new com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene(), config);
	}
}
