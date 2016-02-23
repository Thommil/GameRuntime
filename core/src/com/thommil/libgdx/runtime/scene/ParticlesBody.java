package com.thommil.libgdx.runtime.scene;

import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Defines a physics actor with a soft body or particle system
 *
 * Created by thommil on 14/02/16.
 */
public interface ParticlesBody extends Collidable<ParticleSystemDef, ParticleSystem>{

    /**
     * Gets the radius of particles
     */
    float getParticlesRadius();

    /**
     * Gets the density to the particles
     */
    float getDensity();

    /**
     * Sets the definition of Collidable, implementations
     * should configure Collidable settings in the passed
     * definition.
     *
     * @param particleSystemDef The collidable definition (settings)
     */
    @Override
    void setDefinition(final ParticleSystemDef particleSystemDef);

    /**
     * Called at body creation, implementations can add
     * settings, behaviour and logic from here.
     *
     * @param particleSystem The collidable body instance
     */
    @Override
    void setBody(final ParticleSystem particleSystem);
}
