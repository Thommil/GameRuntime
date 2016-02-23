package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.physics.ParticleSystemActor;


/**
 * Created by tomtom on 03/02/16.
 */
public class WaterActor extends ParticleSystemActor {

    public WaterActor(int layer) {
        super(MathUtils.random(0x7ffffffe), layer, 0.05f);
        this.density = 5f;
    }
}
