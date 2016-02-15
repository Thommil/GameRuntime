package com.thommil.libgdx.runtime.scene.actor.graphics;

import com.thommil.libgdx.runtime.graphics.batch.ParticleEffectBatch;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Particle Actor for rendering particles
 *
 * //TODO Particle rendering and actor
 *
 * Created by thommil on 14/02/16.
 */
public class ParticleEffectActor implements Actor, Renderable<ParticleEffectBatch> {

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return 0;
    }

    /**
     * Returns the layer of the Renderable
     *
     * @return The layer of the Renderable
     */
    @Override
    public int getLayer() {
        return 0;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, ParticleEffectBatch renderer) {

    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {

    }
}
