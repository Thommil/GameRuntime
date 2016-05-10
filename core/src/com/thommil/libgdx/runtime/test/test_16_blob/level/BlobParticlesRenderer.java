package com.thommil.libgdx.runtime.test.test_16_blob.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.graphics.renderer.particles.TexturedParticlesBatchRenderer;

/**
 * Created by tomtom on 03/05/16.
 */
public class BlobParticlesRenderer extends TexturedParticlesBatchRenderer {

    public BlobParticlesRenderer(int maxParticles) {
        super(maxParticles);
    }

    /**
     * Called at beginning of rendering
     */
    @Override
    public void begin() {
        //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        super.begin();
    }



    /**
     * Called at ending of rendering
     */
    @Override
    public void end() {
        super.end();
        Gdx.gl.glBlendFunc(Runtime.getInstance().getSettings().graphics.blendSrcFunc,
                Runtime.getInstance().getSettings().graphics.blendDstFunc);
    }
}
