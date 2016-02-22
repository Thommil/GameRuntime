package com.thommil.libgdx.runtime.scene.actor.physics;

import com.thommil.libgdx.runtime.graphics.batch.ParticlesBatch;
import com.thommil.libgdx.runtime.scene.ParticlesBody;
import com.thommil.libgdx.runtime.scene.Renderable;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Physics particles system actor container
 *
 * Created by thommil on 14/02/16.
 */
public abstract class ParticleSystemActor extends AbstractStepable implements ParticlesBody, Renderable<ParticlesBatch> {

    public ParticleSystem particleSystem;

    protected int layer = 0;

    protected final boolean colored;

    /**
     * Default constructor without color support
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer of the renderable in the scene
     */
    public ParticleSystemActor(final int id, final int layer) {
        this(id, layer, false);
    }

    /**
     * Full constructor
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer of the renderable in the scene
     * @param colored If true, the colored are sent to the renderer
     */
    public ParticleSystemActor(final int id, final int layer, boolean colored) {
        super(id);
        this.layer = layer;
        this.colored = colored;
    }

    /**
     * Set body instance of the Collidable
     *
     * @param particleSystem
     */
    @Override
    public void setBody(ParticleSystem particleSystem) {
        this.particleSystem = particleSystem;
    }

    /**
     * Gets the definition of ParticlesBody
     *
     * @return The definition of the soft body in a particle system
     */
    @Override
    public abstract ParticleSystemDef getDefinition();

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

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.particleSystem.destroyParticleSystem();
    }
}
