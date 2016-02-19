package com.thommil.libgdx.runtime.scene.actor.graphics;

import com.thommil.libgdx.runtime.graphics.batch.SpriteBatch;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Particle Actor for rendering particles
 *
 * Created by thommil on 14/02/16.
 */
public class ParticleActor implements Actor, Renderable<SpriteBatch> {

    protected int id;
    protected int layer = 0;

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

    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {

    }
}
