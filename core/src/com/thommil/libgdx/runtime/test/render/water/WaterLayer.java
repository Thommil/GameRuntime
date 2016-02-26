package com.thommil.libgdx.runtime.test.render.water;

import com.thommil.libgdx.runtime.scene.layer.ParticlesBatchLayer;

/**
 * Created by tomtom on 23/02/16.
 */
public class WaterLayer extends ParticlesBatchLayer {

    public static final float PARTICLES_SCALE_FACTOR = 3f;

    static {
        ParticlesBatchLayer.setGlobalSize(5000);
    }

    public WaterLayer() {
        super(5000);
        this.setScaleFactor(PARTICLES_SCALE_FACTOR);
    }

}
