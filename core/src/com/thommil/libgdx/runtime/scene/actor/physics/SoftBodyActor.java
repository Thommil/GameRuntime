package com.thommil.libgdx.runtime.scene.actor.physics;

import com.thommil.libgdx.runtime.graphics.batch.ParticlesBatch;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * SoftBody actor (renderable using a ParticlesBatch)
 *
 * Created by thommil on 14/02/16.
 */
public abstract class SoftBodyActor extends ParticleSystemActor implements Renderable<ParticlesBatch> {

    protected int layer = 0;

    protected final boolean colored;

    /**
     * Default constructor without color support
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer of the renderable in the scene
     */
    public SoftBodyActor(final int id, final int layer) {
        this(id, layer, false);
    }

    /**
     * Full constructor
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer of the renderable in the scene
     * @param colored If true, the colored are sent to the renderer
     */
    public SoftBodyActor(final int id, final int layer, boolean colored) {
        super(id);
        this.layer = layer;
        this.colored = colored;
    }

    /**
     * Sets the layer index
     *
     * @param layer The layer index
     */
    public void setLayer(final int layer){
        this.layer = layer;
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
    public void render(float deltaTime, ParticlesBatch renderer) {
        if(this.colored) {
            renderer.draw(this.particleSystem.getParticlePositionAndColorBufferArray(true), this.particleSystem.getParticleRadius());
        }
        else{
            renderer.draw(this.particleSystem.getParticlePositionBufferArray(true), this.particleSystem.getParticleRadius());
        }
    }
}
