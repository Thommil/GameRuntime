package com.thommil.libgdx.runtime.test.render.water;

import com.thommil.libgdx.runtime.scene.actor.physics.SoftBodyActor;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Created by tomtom on 03/02/16.
 */
public class WaterActor extends SoftBodyActor {

    public WaterActor(int layer) {
        this.setLayer(layer);
    }

    /**
     * Gets the definition of SoftBody
     *
     * @return The definition of the soft body in a particle system
     */
    @Override
    public ParticleSystemDef getDefinition() {
        ParticleSystemDef particleSystemDef = new ParticleSystemDef();
        particleSystemDef.radius = 0.05f;
        particleSystemDef.density = 1f;
        particleSystemDef.dampingStrength = 1.5f;
        return particleSystemDef;
    }

}
