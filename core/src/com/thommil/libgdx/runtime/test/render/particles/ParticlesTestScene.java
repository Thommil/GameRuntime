package com.thommil.libgdx.runtime.test.render.particles;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.graphics.ParticleActor;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Test for a Simple Sprite display
 */
public class ParticlesTestScene extends Game  implements InputProcessor{

	Scene scene;
	ParticleActor actor;
	ParticleEffect effect;

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_INFO);

		//Scene
		Scene.Settings settings = new Scene.Settings();
		settings.viewport.minWorldWidth = 4;
		settings.viewport.minWorldHeight = 4;
		scene = new Scene(settings);

		//Layers
		SpriteCacheLayer backgroundLayer = new SpriteCacheLayer();
		scene.addLayer(backgroundLayer);
		Texture backgroundTexture = new Texture(Gdx.files.internal("floor_tiles.jpg"));
		backgroundTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
		backgroundLayer.beginCache();
		scene.addActor(new StaticActor(MathUtils.random(0x7ffffffe), 0, backgroundTexture,-5,-5,10,10,0,1,1,0, Color.WHITE.toFloatBits()));
		backgroundLayer.endCache();

		scene.addLayer(new SpriteBatchLayer(100));

		//Actor
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("effects/particles.p"), Gdx.files.internal("effects"));
		actor = new ParticleActor(0, 1, effect, 10);
		scene.addActor(actor);

		Gdx.input.setInputProcessor(this);

		//Profiler
		SceneProfiler.profile(scene);

		//Start
		this.setScreen(scene);
	}

	final Vector2 tmpScreenVector = new Vector2();

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		tmpScreenVector.set(screenX,screenY);
		final Vector2 worldVector =  this.scene.getViewport().unproject(tmpScreenVector);
		if(effect != null) effect.allowCompletion();
		effect = actor.spawn(true,worldVector.x,worldVector.y,false,false,0.005f);
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
		if(effect != null) effect.allowCompletion();
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		tmpScreenVector.set(screenX,screenY);
		final Vector2 worldVector =  this.scene.getViewport().unproject(tmpScreenVector);
		if(effect != null) {
			effect.getEmitters().get(0).setPosition(worldVector.x, worldVector.y);
			effect.getEmitters().get(1).allowCompletion();
			effect.getEmitters().get(2).setPosition(worldVector.x, worldVector.y);
			effect.getEmitters().get(3).allowCompletion();
		}

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
