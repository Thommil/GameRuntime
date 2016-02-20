package com.thommil.libgdx.runtime.test.render.softbody;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.physics.SoftBodyActor;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Created by tomtom on 03/02/16.
 */
public class SoftbodyRenderActor extends SoftBodyActor {

    public SoftbodyRenderActor() {
        super(MathUtils.random(0x7ffffffe), 1);
    }

    /**
     * Gets the definition of SoftBody
     *
     * @return The definition of the soft body in a particle system
     */
    @Override
    public ParticleSystemDef getDefinition() {
        ParticleSystemDef particleSystemDef = new ParticleSystemDef();
        particleSystemDef.radius = 0.1f;
        particleSystemDef.elasticStrength = 1f;
        particleSystemDef.density = 2f;
        particleSystemDef.ejectionStrength = 2f;
        particleSystemDef.dampingStrength = 2f;
        particleSystemDef.repulsiveStrength = 2f;
        
        return particleSystemDef;
    }
}
