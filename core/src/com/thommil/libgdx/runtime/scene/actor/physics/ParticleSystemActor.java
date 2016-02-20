package com.thommil.libgdx.runtime.scene.actor.physics;

import com.thommil.libgdx.runtime.scene.SoftBody;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Physics particles system actor container (no rendering)
 *
 * Created by thommil on 14/02/16.
 */
public abstract class ParticleSystemActor extends AbstractStepable implements SoftBody {

    public ParticleSystem particleSystem;

    /**
     * Default constructor
     *
     * @param id The ID of the Actor in the scene
     */
    public ParticleSystemActor(final int id) {
        super(id);
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
