package com.thommil.libgdx.runtime.scene.layer;

import com.thommil.libgdx.runtime.graphics.renderer.particles.ParticlesBatchRenderer;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * ParticlesBody layer using ParticlesBatchRenderer as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class ParticlesBatchLayer extends Layer{

    protected float scaleFactor = 1f;

    final protected ParticlesBatchRenderer renderer;

    /**
     * Default constructor
     *
     * @param maxParticles The maximum particles in this layer
     */
    public ParticlesBatchLayer(final int maxParticles) {
        super(maxParticles);
        this.renderer = new ParticlesBatchRenderer(maxParticles);
    }

    /**
     * Constructor with custom renderer
     *
     * @param initialCapacity The maximum particles in this layer
     * @param customRenderer The custom renderer to use
     */
    public ParticlesBatchLayer(final int initialCapacity, final ParticlesBatchRenderer customRenderer) {
        super(initialCapacity);
        this.renderer = customRenderer;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        renderer.setCombinedMatrix(this.camera.combined);
        renderer.begin();
        for(Renderable renderable : this.renderables){
            renderable.render(deltaTime,renderer);
        }
        renderer.end();
    }

    /**
     * Called when layer is showed for subclasses
     */
    @Override
    protected void onShow() {
        //NOP
    }

    /**
     * Called when layer is hidden
     */
    @Override
    protected void onHide() {
        //NOP
    }

    /**
     * Called when layer is resized
     *
     * @param width
     * @param height
     */
    @Override
    protected void onResize(int width, int height) {
        renderer.setParticlesScale((Math.min(width / this.camera.viewportWidth, height / this.camera.viewportHeight)) * scaleFactor);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        renderer.dispose();
    }
}
