package com.thommil.libgdx.runtime.test.render.normalmap;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.scene.layer.CacheLayer;
import com.thommil.libgdx.runtime.test.basic.cache.PlanetActor;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

//Lot of glitches but basically working
public class NormalTestScene extends Game implements InputProcessor{

	NormalCacheRenderer bgRenderer;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 16;
		settings.viewport.minWorldHeight = 9;
		final Scene scene = new Scene(settings);

		//Layers
		bgRenderer = new NormalCacheRenderer(1);
		CacheLayer bgLayer = new CacheLayer(1,bgRenderer);
		scene.addLayer(bgLayer);

		scene.addLayer(new Layer(0) {

			private boolean hasInit = false;

			@Override
			public void render(float deltaTime) {

			}

			@Override
			protected void onShow() {

			}

			@Override
			protected void onHide() {

			}

			@Override
			protected void onResize(int width, int height) {
				bgRenderer.setScreenSize(width, height);
				if(!hasInit){
					bgRenderer.setLightPosition(width/2, height/2);
					hasInit = true;
				}
			}

			@Override
			public void dispose() {

			}
		});

		//Actor
		TextureSet bgTextureSet = new TextureSet(new Texture(Gdx.files.internal("brick_diff.png"))
												,new Texture(Gdx.files.internal("brick_nor.png")));
		bgTextureSet.setWrapAll(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);

		bgLayer.beginCache();
		scene.addActor(new StaticActor(0,0,bgTextureSet,-8,-5,16,10,0,2,2,0, Color.WHITE.toFloatBits()));
		bgLayer.endCache();

		//Profiler
		SceneProfiler.profile(scene);

		Gdx.input.setInputProcessor(this);

		//Start
		this.setScreen(scene);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		bgRenderer.setLightPosition(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		bgRenderer.setLightPosition(screenX, screenY);
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
