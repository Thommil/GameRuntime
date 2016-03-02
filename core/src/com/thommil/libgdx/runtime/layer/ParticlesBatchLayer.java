package com.thommil.libgdx.runtime.layer;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.graphics.renderer.particles.ParticlesBatchRenderer;
import com.thommil.libgdx.runtime.actor.graphics.Renderable;

/**
 * ParticlesBody layer using ParticlesBatchRenderer as renderer
 *
 * @author thommil on 03/02/16.
 */
public class ParticlesBatchLayer extends Layer{

    protected float scaleFactor = 1f;

    private static int size = 1000;
    private static int currentConsumersCount = 0;
    private static ParticlesBatchRenderer sharedRenderer;

    protected final ParticlesBatchRenderer renderer;

    /**
     * Set global batch size
     *
     * @param size The maximum size of the global cache shared among layers
     */
    public static void setGlobalSize(final int size){
        if(ParticlesBatchLayer.sharedRenderer != null){
            throw new GameRuntimeException("Global batch size must be set before creating a new shared ParticlesBatchLayer");
        }
        ParticlesBatchLayer.size = size;
    }

    /**
     * Default constructor using the default shared renderer
     *
     * @param viewport The viewport bound to the layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     */
    public ParticlesBatchLayer(final Viewport viewport, final int initialCapacity) {
        super(viewport, initialCapacity);
        if(ParticlesBatchLayer.sharedRenderer == null){
            ParticlesBatchLayer.sharedRenderer = new ParticlesBatchRenderer(ParticlesBatchLayer.size);
        }
        this.renderer = ParticlesBatchLayer.sharedRenderer;
        this.currentConsumersCount++;
    }

    /**
     * Constructor with custom renderer
     *
     * @param viewport The viewport bound to the layer
     * @param initialCapacity TThe initial capacity of the layer (number of actors)
     * @param customRenderer The custom renderer to use
     */
    public ParticlesBatchLayer(final Viewport viewport, final int initialCapacity, final ParticlesBatchRenderer customRenderer) {
        super(viewport, initialCapacity);
        this.renderer = customRenderer;
    }

    /**
     * Rendering scale factor around particles
     *
     * @param scaleFactor The scale factor of particles
     */
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
        if(!this.isHidden()) {
            this.renderer.setCombinedMatrix(this.viewport.getCamera().combined);
            this.renderer.begin();
            for (Renderable renderable : this.renderables) {
                renderable.render(deltaTime, this.renderer);
            }
            this.renderer.end();
        }
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
        renderer.setParticlesScale((Math.min(width / this.viewport.getWorldWidth(), height / this.viewport.getWorldHeight())) * this.scaleFactor);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        if(this.renderer == ParticlesBatchLayer.sharedRenderer){
            currentConsumersCount--;
            if(currentConsumersCount == 0) {
                ParticlesBatchLayer.sharedRenderer.dispose();
                ParticlesBatchLayer.sharedRenderer = null;
            }
        }
        else{
            this.renderer.dispose();
        }
    }
}