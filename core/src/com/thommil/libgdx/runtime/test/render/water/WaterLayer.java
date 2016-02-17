package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.scene.layer.SoftBodyBatchLayer;

/**
 * Created by tomtom on 17/02/16.
 */
public class WaterLayer extends SoftBodyBatchLayer {

    public WaterLayer(){
        super(new WaterBatch());
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        ((WaterBatch)this.renderer).onResize(width,height,this.camera.viewportWidth, this.camera.viewportHeight);
    }
}
