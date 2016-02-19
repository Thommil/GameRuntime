package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.graphics.batch.SoftBodyBatch;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.SoftBody;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * SoftBody actor (renderable using a SoftBodyBatch)
 *
 * Created by thommil on 14/02/16.
 */
public abstract class SoftBodyActor extends ParticleSystemActor implements Renderable<SoftBodyBatch> {

    public static final int VERTEX_SIZE = 2;

    protected int layer = 0;

    public SoftBodyActor(final int id, final int layer) {
        super(id);
        this.layer = layer;
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
}
