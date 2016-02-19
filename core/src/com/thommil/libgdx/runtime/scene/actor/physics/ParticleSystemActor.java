package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.graphics.batch.SoftBodyBatch;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.SoftBody;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Physics particles system actor container (no rendering)
 *
 * Created by thommil on 14/02/16.
 */
public abstract class ParticleSystemActor implements SoftBody {

    public ParticleSystem particleSystem;

    protected final int id;

    public ParticleSystemActor(final int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return this.id;
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
