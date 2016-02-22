package com.thommil.libgdx.runtime.test.physics.softbody;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.physics.ParticleSystemActor;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Created by tomtom on 03/02/16.
 */
public class SoftbodyActor extends ParticleSystemActor {

    public SoftbodyActor() {
        super(MathUtils.random(0x7ffffffe), 1, true);
    }

    /**
     * Gets the definition of ParticlesBody
     *
     * @return The definition of the soft body in a particle system
     */
    @Override
    public ParticleSystemDef getDefinition() {
        ParticleSystemDef particleSystemDef = new ParticleSystemDef();
        particleSystemDef.radius = 0.05f;
        particleSystemDef.elasticStrength = 1.2f;
        particleSystemDef.density = 0.3f;
        //particleSystemDef.ejectionStrength = 5f;
        //particleSystemDef.dampingStrength = 2f;
        //particleSystemDef.repulsiveStrength = 0.1f;
        //particleSystemDef.viscousStrength = 4f;
        return particleSystemDef;
    }


}
