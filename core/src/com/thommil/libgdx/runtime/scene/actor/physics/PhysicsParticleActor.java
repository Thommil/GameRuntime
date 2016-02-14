package com.thommil.libgdx.runtime.scene.actor.physics;

import com.thommil.libgdx.runtime.graphics.batch.ParticleSystemBatch;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Physics particle system ator container
 *
 * Created by thommil on 14/02/16.
 */
public class PhysicsParticleActor implements Actor, Renderable<ParticleSystemBatch>{

    protected int layer = 0;

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
    public void render(float deltaTime, ParticleSystemBatch renderer) {

    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {

    }
}
