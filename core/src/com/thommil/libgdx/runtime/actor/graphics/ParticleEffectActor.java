package com.thommil.libgdx.runtime.actor.graphics;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Array;
import com.thommil.libgdx.runtime.graphics.animation.RotateAnimation;
import com.thommil.libgdx.runtime.graphics.animation.ScaleAnimation;
import com.thommil.libgdx.runtime.graphics.animation.TranslateAnimation;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.actor.Actor;

/**
 * Particle Actor for rendering particles using an inner Pool
 *
 * @author thommil on 03/02/16.
 */
public class ParticleEffectActor extends Actor implements Renderable<SpriteBatchRenderer> {

    protected final ParticleEffectPool particleEffectPool;
    protected final Array<ParticleEffectPool.PooledEffect> particleEffects;

    /**
     * Default constructor
     *
     * @param id Actor ID in the scene
     * @param particleEffect The particle effect to spawn
     * @param maxParticlesEffects The maximum amount on effects in this actor
     */
    public ParticleEffectActor(int id, ParticleEffect particleEffect, final int maxParticlesEffects) {
        super(id);
        particleEffect.setEmittersCleanUpBlendFunction(false);
        this.particleEffectPool = new ParticleEffectPool(particleEffect, maxParticlesEffects, maxParticlesEffects);
        this.particleEffects = new Array<ParticleEffectPool.PooledEffect>(false, maxParticlesEffects);
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        for (int i = this.particleEffects.size - 1; i >= 0; i--) {
            final ParticleEffectPool.PooledEffect particleEffect = this.particleEffects.get(i);
            particleEffect.draw(renderer, deltaTime);
            if (particleEffect.isComplete()) {
                particleEffect.reset();
                this.particleEffects.removeIndex(i);
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
     * Play a given translate animation in given ParticleEffect at specified state time
     *
     * @param particleEffect The particle effect to animate
     * @param animation The translate animation to use
     * @param stateTime The state time in seconds
     */
    public ParticleEffect playAnimation(final ParticleEffect particleEffect, final TranslateAnimation animation, final float stateTime){
        final TranslateAnimation.KeyFrame translation = animation.getKeyFrame(stateTime);
        for(final ParticleEmitter particleEmitter : particleEffect.getEmitters()){
            particleEmitter.setPosition(particleEmitter.getX() + translation.x, particleEmitter.getY() +translation.y);
            particleEmitter.setFlip(translation.flipX, translation.flipY);
        }
        return particleEffect;
    }

    /**
     * Play a given scale animation in given ParticleEffect at specified state time
     *
     * @param particleEffect The particle effect to animate
     * @param animation The translate animation to use
     * @param stateTime The state time in seconds
     */
    public ParticleEffect playAnimation(final ParticleEffect particleEffect, final ScaleAnimation animation, final float stateTime){
        final ScaleAnimation.KeyFrame scale = animation.getKeyFrame(stateTime);
        particleEffect.scaleEffect(Math.max(scale.x,scale.y));
        return particleEffect;
    }

    /**
     * Play a given rotate animation in given ParticleEffect at specified state time
     *
     * @param particleEffect The particle effect to animate
     * @param animation The translate animation to use
     * @param stateTime The state time in seconds
     */
    public ParticleEffect playAnimation(final ParticleEffect particleEffect, final RotateAnimation animation, final float stateTime){
        final RotateAnimation.KeyFrame rotation = animation.getKeyFrame(stateTime);
        for(final ParticleEmitter particleEmitter : particleEffect.getEmitters()){
            particleEmitter.getAngle().setHigh(particleEmitter.getAngle().getHighMin() + rotation.angle);
            particleEmitter.getAngle().setLow(particleEmitter.getAngle().getLowMin() + rotation.angle);
        }
        return particleEffect;
    }

    /**
     * Releases a previously spawned ParticleEffect
     */
    public void release(final ParticleEffect particleEffect){
        particleEffect.reset();
        this.particleEffects.removeValue((ParticleEffectPool.PooledEffect)particleEffect, true);
        ((ParticleEffectPool.PooledEffect)particleEffect).free();
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
