package com.thommil.libgdx.runtime.test.physics.softbody;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.physics.SoftBodyActor;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Created by tomtom on 03/02/16.
 */
public class SoftbodyActor extends SoftBodyActor {

    public SoftbodyActor() {
        super(MathUtils.random(0x7ffffffe), 0);
    }

    /**
     * Gets the definition of SoftBody
     *
     * @return The definition of the soft body in a particle system
     */
    @Override
    public ParticleSystemDef getDefinition() {
        ParticleSystemDef particleSystemDef = new ParticleSystemDef();
        particleSystemDef.radius = 0.025f;
        return particleSystemDef;
    }
}
