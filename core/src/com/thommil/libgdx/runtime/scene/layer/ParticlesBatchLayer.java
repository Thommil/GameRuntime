package com.thommil.libgdx.runtime.scene.layer;

import com.thommil.libgdx.runtime.graphics.batch.ParticlesBatch;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * ParticlesBody layer using ParticlesBatch as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class ParticlesBatchLayer extends Layer{

    protected float scaleFactor = 1f;

    final protected ParticlesBatch renderer;

    public ParticlesBatchLayer(final int maxParticles) {
        super(maxParticles);
        this.renderer = new ParticlesBatch(maxParticles);
    }

    public ParticlesBatchLayer(final int initialCapacity, final ParticlesBatch customRenderer) {
        super(initialCapacity);
        this.renderer = customRenderer;
    }

    @Override
    protected void onResize(int width, int height) {
        renderer.setParticlesScale((Math.min(width / this.camera.viewportWidth, height / this.camera.viewportHeight)) * scaleFactor);
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
    public void onShow() {
        //NOP
    }

    @Override
    public void onHide() {
        //NOP
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
