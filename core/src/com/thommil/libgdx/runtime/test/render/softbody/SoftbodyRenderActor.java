package com.thommil.libgdx.runtime.test.render.softbody;

import com.badlogic.gdx.graphics.Color;
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
        particleSystemDef.radius = 0.04f;
        particleSystemDef.density = 1f;
        particleSystemDef.viscousStrength = 0.8f;
        particleSystemDef.dampingStrength = 1f;
        particleSystemDef.elasticStrength = 0.3f;
        particleSystemDef.strictContactCheck = true;
        return particleSystemDef;
    }

    /**
     * Returns the colors of the particles
     */
    public float[] getColors(){
        float []colors = new float[1089];

        for(int particle=0; particle<1089;particle++){
            float color;
            if(particle < 132) color = Color.ORANGE.toFloatBits();
            else if(particle < 264) color = Color.BLUE.toFloatBits();
            else if(particle < 396) color = Color.GREEN.toFloatBits();
            else if(particle < 528) color = Color.BROWN.toFloatBits();
            else if(particle < 660) color = Color.VIOLET.toFloatBits();
            else if(particle < 792) color = Color.TEAL.toFloatBits();
            else if(particle < 924) color = Color.SALMON.toFloatBits();
            else color = Color.RED.toFloatBits();

            colors[particle] = color;
        }

        return  colors;
    }
}
