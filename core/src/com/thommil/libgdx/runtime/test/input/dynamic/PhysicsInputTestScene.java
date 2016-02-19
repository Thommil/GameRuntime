package com.thommil.libgdx.runtime.test.input.dynamic;

import com.badlogic.gdx.*;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Test for a physic actor using inputs
 */
public class PhysicsInputTestScene extends Game implements InputProcessor{

	private ShipActor shipActor;
	private Scene scene;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 20;
		settings.viewport.minWorldHeight = 20;
		settings.physics.gravity = new float[]{0f,0f};
		//settings.physics.debug=true;
		scene = new Scene(settings);

		//Layers
		scene.addLayer(new SpriteBatchLayer(11));

		//Actor
		for(int i=0; i < 10; i++) {
			scene.addActor(new PlanetActor());
		}
		shipActor = new ShipActor();
		scene.addActor(shipActor);

		Gdx.input.setInputProcessor(this);

		//Profiler
		SceneProfiler.profile(scene);

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
	public boolean touchDown(final int screenX, final int screenY, int pointer, int button) {
		this.scene.runOnPhysicsThread(new Runnable() {
			@Override
			public void run() {
				PhysicsInputTestScene.this.shipActor.target(PhysicsInputTestScene.this.scene.getViewport().unproject(mousePointerVec.set(screenX,screenY)));
				PhysicsInputTestScene.this.shipActor.follow(true);
			}
		});

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		this.scene.runOnPhysicsThread(new Runnable() {
			@Override
			public void run() {
				PhysicsInputTestScene.this.shipActor.follow(false);
			}
		});
		return false;
	}

	@Override
	public boolean touchDragged(final int screenX, final int screenY, int pointer) {
		this.scene.runOnPhysicsThread(new Runnable() {
			@Override
			public void run() {
				PhysicsInputTestScene.this.shipActor.target(PhysicsInputTestScene.this.scene.getViewport().unproject(mousePointerVec.set(screenX,screenY)));
			}
		});
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