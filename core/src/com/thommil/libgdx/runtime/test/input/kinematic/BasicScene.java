package com.thommil.libgdx.runtime.test.input.kinematic;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.BasicSpriteLayer;

/**
 * Test for a kinematic actor using inputs
 */
public class BasicScene extends Game implements InputProcessor {

	private KinematicActor shipActor;
	private Scene scene;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 20;
		settings.viewport.minWorldHeight = 20;
		scene = new Scene(settings);

		//Layers
		scene.addLayer(0,new BasicSpriteLayer(100));

		//Actor
		shipActor = new KinematicActor();
		scene.addActor(shipActor);

		Gdx.input.setInputProcessor(this);

		//Start
		this.setScreen(scene);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode){
			case Input.Keys.UP:
				this.shipActor.forward(true);
				break;
			case Input.Keys.DOWN:
				this.shipActor.backward(true);
				break;
			case Input.Keys.LEFT:
				this.shipActor.left(true);
				break;
			case Input.Keys.RIGHT:
				this.shipActor.right(true);
				break;
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode){
			case Input.Keys.UP:
				this.shipActor.forward(false);
				break;
			case Input.Keys.DOWN:
				this.shipActor.backward(false);
				break;
			case Input.Keys.LEFT:
				this.shipActor.left(false);
				break;
			case Input.Keys.RIGHT:
				this.shipActor.right(false);
				break;
		}

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	private Vector2 mousePointerVec = new Vector2();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		this.shipActor.target(this.scene.getViewport().unproject(mousePointerVec.set(screenX,screenY)));
		this.shipActor.follow(true);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this.shipActor.follow(false);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		this.shipActor.target(this.scene.getViewport().unproject(mousePointerVec.set(screenX,screenY)));
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
