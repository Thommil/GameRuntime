package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.physics.SoftBodyActor;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Created by tomtom on 03/02/16.
 */
public class WaterActor extends SoftBodyActor {

    public WaterActor(int layer) {
        super(MathUtils.random(0x7ffffffe), layer);
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
        particleSystemDef.density = 5f;
        return particleSystemDef;
    }

}
