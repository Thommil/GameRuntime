package com.thommil.libgdx.runtime.scene.layer;

import com.thommil.libgdx.runtime.graphics.batch.SoftBodyBatch;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * SoftBody layer using SoftBodyBatch as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class SoftBodyBatchLayer extends Layer{

    final protected SoftBodyBatch renderer;

    public SoftBodyBatchLayer(final int maxParticles) {
        super(maxParticles);
        this.renderer = new SoftBodyBatch(maxParticles);
    }

    public SoftBodyBatchLayer(final int maxParticles, final SoftBodyBatch customRenderer) {
        super(maxParticles);
        this.renderer = customRenderer;
    }

    public SoftBodyBatchLayer(final SoftBodyBatch customRenderer) {
        super(DEFAULT_INITIAL_CAPACITY);
        this.renderer = customRenderer;
    }

    @Override
    public void onShow() {
        //NOP
    }

    @Override
    public void onHide() {
        //NOP
    }

    @Override
    protected void onResize(int width, int height) {
        renderer.setParticlesScale((Math.min(width / this.camera.viewportWidth, height / this.camera.viewportHeight)) * 1.5f);
    }

    @Override
    public void render(float deltaTime) {
        renderer.setCombinedMatrix(this.camera.combined);
        renderer.begin();
        for(Renderable renderable : this.renderables){
            renderable.render(deltaTime,renderer);
        }
        renderer.end();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
