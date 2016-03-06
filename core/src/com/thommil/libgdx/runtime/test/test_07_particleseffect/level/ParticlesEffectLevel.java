package com.thommil.libgdx.runtime.test.test_07_particleseffect.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.graphics.ParticleEffectActor;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;

/**
 * @author  Thommil on 04/03/16.
 */
public class ParticlesEffectLevel implements InputProcessor, Disposable {

    ParticleEffect particleEffect;
    ParticleEffectActor particleEffectActor;

    SpriteBatchLayer spriteBatchLayer;

    public ParticlesEffectLevel() {
        SpriteBatchLayer.setGlobalSize(1000);
        spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(), 1000);

        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("effects/particles.p"), Gdx.files.internal("effects"));
        particleEffectActor = new ParticleEffectActor(0, particleEffect, 10);
        spriteBatchLayer.addActor(particleEffectActor);

        Runtime.getInstance().addLayer(spriteBatchLayer);
        RuntimeProfiler.profile();
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.spriteBatchLayer.dispose();
        this.particleEffect.dispose();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        final Vector2 worldVector =  Runtime.getInstance().getViewport().unproject(new Vector2(screenX, screenY));
        if(particleEffect != null) this.particleEffect.allowCompletion();
        particleEffect = this.particleEffectActor.spawn(true,worldVector.x,worldVector.y,false,false,0.005f);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(particleEffect != null) particleEffect.allowCompletion();
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        final Vector2 worldVector =  Runtime.getInstance().getViewport().unproject(new Vector2(screenX, screenY));
        if(particleEffect != null) {
            particleEffect.setPosition(worldVector.x, worldVector.y);
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }


    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
