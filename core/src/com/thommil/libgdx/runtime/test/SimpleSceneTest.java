package com.thommil.libgdx.runtime.test;

import com.badlogic.gdx.Game;
import com.thommil.libgdx.runtime.Scene;

/**
 * Test for a Simple Scene display :
 * 	- ...
 */
public class SimpleSceneTest extends Game {

	@Override
	public void create () {
		Scene defaultScene = new Scene();
		this.setScreen(defaultScene);
	}
}
