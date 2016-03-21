package com.thommil.libgdx.runtime.actor.physics;

import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Defines a physics actor with a soft body or particle system
 *
 * @author thommil on 03/02/16.
 */
public interface ParticlesBody extends Collidable<ParticleSystemDef, ParticleSystem> {

    /**
     * Gets the radius of particles
     */
    float getParticlesRadius();

    /**
     * Gets the density to the particles
     */
    float getDensity();

    /**
     * Gets the definition of Collidable.
     *
     * @return definition The collidable definition (settings)
     */
    @Override
    ParticleSystemDef getDefinition();

    /**
     * Called at body creation, implementations can add
     * settings, behaviour and logic from here.
     *
     * ParticlesBody differs from RididBody as new shapes and
     * fixtures must be added after creation of the ParticleSystem.
     *
     * This can be done by adding ParticleSystemGroup and Particles here.
     *
     * @param particleSystem The collidable body instance
     */
    @Override
    void setBody(final ParticleSystem particleSystem);

    /**
     * Get the collidable body
     */
    @Override
    ParticleSystem getBody();
}
