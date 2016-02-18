package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.graphics.batch.SoftBodyBatch;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.SoftBody;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Physics particle system ator container
 *
 * Created by thommil on 14/02/16.
 */
public abstract class SoftBodyActor implements SoftBody, Renderable<SoftBodyBatch> {

    public static final int VERTEX_SIZE = 2;

    public ParticleSystem particleSystem;

    protected int layer = 0;

    protected final int id;

    public SoftBodyActor(final int id, final int layer) {
        this.id = id;
        this.layer = layer;
    }

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return this.id;
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
    public void render(float deltaTime, SoftBodyBatch renderer) {
        renderer.draw(this.particleSystem.getParticlePositionBufferArray(true), this.particleSystem.getParticleRadius());
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
     * Gets the definition of SoftBody
     *
     * @return The definition of the soft body in a particle system
     */
    @Override
    public abstract ParticleSystemDef getDefinition();

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.particleSystem.destroyParticleSystem();
    }
}
