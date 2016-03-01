package com.thommil.libgdx.runtime.runtime.layer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.thommil.libgdx.runtime.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.runtime.actor.graphics.Renderable;
import com.thommil.libgdx.runtime.graphics.renderer.Renderer;

/**
 * Basic Sprite layer using shared/custom SpriteBatchRenderer or Batch as renderer
 *
 * @author thommil on 03/02/16.
 */
public class SpriteBatchLayer extends Layer{

    private static int size = 1000;
    private static int currentConsumersCount = 0;
    private static SpriteBatchRenderer sharedRenderer;

    protected final Renderer renderer;

    /**
     * Set global batch size
     *
     * @param size The maximum size of the global cache shared among layers
     */
    public static void setGlobalSize(final int size){
        if(SpriteBatchLayer.sharedRenderer != null){
            throw new GameRuntimeException("Global batch size must be set before creating a new shared SpriteBatchLayer");
        }
        SpriteBatchLayer.size = size;
    }

    /**
     * Default constructor using the default shared renderer
     *
     * @param initialCapacity The initial capacity of the layer (number of actors)
     */
    public SpriteBatchLayer(final int initialCapacity) {
        super(initialCapacity);
        if(SpriteBatchLayer.sharedRenderer == null){
            SpriteBatchLayer.sharedRenderer = new SpriteBatchRenderer(SpriteBatchLayer.size);
        }
        this.renderer = SpriteBatchLayer.sharedRenderer;
        this.currentConsumersCount++;
    }

    /**
     * Constructor with custom renderer
     *
     * @param initialCapacity The initial capacity of the layer (number of actors)
     * @param customRenderer The custom renderer to use
     */
    public SpriteBatchLayer(final int initialCapacity, final Renderer customRenderer) {
        super(initialCapacity);
        this.renderer = customRenderer;
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        if(!this.hidden) {
            if (this.renderer instanceof SpriteBatchRenderer) {
                ((SpriteBatchRenderer) renderer).setCombinedMatrix(this.camera.combined);
            } else if (this.renderer instanceof Batch) {
                ((Batch) renderer).setProjectionMatrix(this.camera.projection);
                ((Batch) renderer).setTransformMatrix(this.camera.view);
            }
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
        //NOP
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        if(this.renderer == SpriteBatchLayer.sharedRenderer){
            currentConsumersCount--;
            if(currentConsumersCount == 0) {
                SpriteBatchLayer.sharedRenderer.dispose();
                SpriteBatchLayer.sharedRenderer = null;
            }
        }
        else{
            this.renderer.dispose();
        }
    }
}
