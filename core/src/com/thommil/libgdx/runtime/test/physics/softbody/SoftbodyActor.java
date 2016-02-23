package com.thommil.libgdx.runtime.test.physics.softbody;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.physics.ParticleSystemActor;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Created by tomtom on 03/02/16.
 */
public class SoftbodyActor extends ParticleSystemActor {

    public SoftbodyActor() {
        super(MathUtils.random(0x7ffffffe), 1, 0.5f, false);
        this.density = 1f;
    }

    @Override
    public void setDefinition(ParticleSystemDef particleSystemDef) {
        super.setDefinition(particleSystemDef);
        //particleSystemDef.elasticStrength = 1.2f;
        particleSystemDef.strictContactCheck = true;
        //particleSystemDef.ejectionStrength = 5f;
        //particleSystemDef.dampingStrength = 2f;
        //particleSystemDef.repulsiveStrength = 0.1f;
        //particleSystemDef.viscousStrength = 2f;
    }
}
