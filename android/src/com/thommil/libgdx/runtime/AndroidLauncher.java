package com.thommil.libgdx.runtime;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.thommil.libgdx.runtime.test.basic.BasicScene;
//import com.thommil.libgdx.runtime.test.physics.basic.PhysicsScene;
import com.thommil.libgdx.runtime.test.physics.stress.PhysicsScene;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new PhysicsScene(), config);
	}
}
