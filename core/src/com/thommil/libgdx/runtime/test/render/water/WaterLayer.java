package com.thommil.libgdx.runtime.test.render.water;

import com.thommil.libgdx.runtime.scene.layer.ParticlesBatchLayer;

/**
 * Created by tomtom on 17/02/16.
 */
public class WaterLayer extends ParticlesBatchLayer {

    public WaterLayer(){
        super(1,new WaterBatch());
        this.setScaleFactor(1.5f);
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        ((WaterBatch)this.renderer).onResize(width,height,this.camera.viewportWidth, this.camera.viewportHeight);
    }
}
