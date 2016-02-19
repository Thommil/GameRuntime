package com.thommil.libgdx.runtime.scene.actor.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Array;
import com.thommil.libgdx.runtime.graphics.batch.SpriteBatch;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Particle Actor for rendering particles using an inner Pool
 *
 * Created by thommil on 14/02/16.
 */
public class ParticleActor implements Actor, Renderable<SpriteBatch> {

    protected final int id;
    protected int layer = 0;
    protected final ParticleEffectPool particleEffectPool;
    protected final Array<ParticleEffectPool.PooledEffect> particleEffects;

    public ParticleActor(int id, int layer, ParticleEffect particleEffect, final int maxParticles) {
        this.id = id;
        this.layer = layer;
        this.particleEffectPool = new ParticleEffectPool(particleEffect, maxParticles, maxParticles);
        this.particleEffects = new Array<ParticleEffectPool.PooledEffect>(false, maxParticles);
    }

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Returns the layer of the Renderable
     *
     * @return The layer of the Renderable
     */
    @Override
    public int getLayer() {
        return this.layer;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatch renderer) {
        for (int i = particleEffects.size - 1; i >= 0; i--) {
            final ParticleEffectPool.PooledEffect particleEffect = particleEffects.get(i);
            particleEffect.draw(renderer, deltaTime);
            if (particleEffect.isComplete()) {
                particleEffect.reset();
                particleEffects.removeIndex(i);
                particleEffect.free();
            }
        }
    }

    /**
     * Spawns a new particle effect with specified parmaters and returns it
     */
    public ParticleEffect spawn(final boolean autostart, final float x, final float y, final boolean flipX, final boolean flipY, final float scaleFactor){
        final ParticleEffectPool.PooledEffect particleEffect = this.particleEffectPool.obtain();
        particleEffect.setPosition(x,y);
        particleEffect.setFlip(flipX,flipY);
        particleEffect.scaleEffect(scaleFactor);
        if(autostart) {
            particleEffect.start();
        }
        this.particleEffects.add(particleEffect);
        return particleEffect;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.particleEffects.clear();
        this.particleEffectPool.clear();
    }
}
