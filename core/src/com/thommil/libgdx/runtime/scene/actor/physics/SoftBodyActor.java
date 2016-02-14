package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.graphics.batch.ParticleSystemBatch;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.SoftBody;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Physics particle system ator container
 *
 * Created by thommil on 14/02/16.
 */
public abstract class SoftBodyActor implements SoftBody, Renderable<ParticleSystemBatch> {

    protected int layer = 0;

    protected int id;

    protected ParticleSystem particleSystem;

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

    }
}
