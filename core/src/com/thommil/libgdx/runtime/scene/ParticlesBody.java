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
     * Gets the definition of Collidable
     */
    @Override
    ParticleSystemDef getDefinition();

    /**
     * Set body instance of the Collidable
     *
     * @param particleSystem
     */
    @Override
    void setBody(ParticleSystem particleSystem);
}
