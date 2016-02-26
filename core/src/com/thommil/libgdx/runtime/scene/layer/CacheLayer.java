package com.thommil.libgdx.runtime.scene.layer;

import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.graphics.renderer.cache.CacheRenderer;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;

/**
 * Basic Sprite layer using BasicSpriteBatch as sharedRenderer
 *
 * Created by thommil on 03/02/16.
 */
public class CacheLayer extends Layer{

    private static int size = 1000;
    private static int currentCacheCount = 0;
    private static CacheRenderer sharedRenderer;
    private int cacheId;

    protected final CacheRenderer renderer;

    /**
     * Set global cache size
     *
     * @param size The maximum size of the global cache shared among layers
     */
    public static void setSize(final int size){
        if(CacheLayer.sharedRenderer != null){
            throw new GameRuntimeException("CacheLayer size must be set before creating a new CacheLayer");
        }
        CacheLayer.size = size;
    }

    /**
     * Default constructor using the default shared cache
     *
     * @param initialCapacity The initial capacity of the layer
     */
    public CacheLayer(final int initialCapacity) {
        super(initialCapacity);
        if(CacheLayer.sharedRenderer == null){
            CacheLayer.sharedRenderer = new CacheRenderer(CacheLayer.size);
        }
        this.renderer = CacheLayer.sharedRenderer;
        this.currentCacheCount++;
    }

    /**
     * Custom cache renderer constructor
     *
     * @param initialCapacity The initial capacity of the layer
     * @param renderer The custom renderer to use
     */
    public CacheLayer(final int initialCapacity, final CacheRenderer renderer) {
        super(initialCapacity);
        this.renderer = renderer;
    }

    /**
     *  Cals this method to begin cache for underlying CacheRenderer
     */
    public void beginCache(){
        this.renderer.beginCache();
    }

    /**
     *  Cals this method to begin cache for underlying CacheRenderer
     */
    public void endCache(){
        this.cacheId = this.renderer.endCache();
    }

    /**
     * Add a renderable to the layer
     *
     * @param renderable The renderable to add
     */
    @Override
    public void add(Renderable renderable) {
        if(renderable instanceof StaticActor){
            this.renderer.add((StaticActor)renderable);
        }
        else if(renderable instanceof SpriteActor){
            this.renderer.add((SpriteActor)renderable);
        }
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        if(!this.hidden) {
            this.renderer.setCombinedMatrix(this.camera.combined);
            this.renderer.begin();
            this.renderer.draw(this.cacheId);
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
        if(this.renderer == CacheLayer.sharedRenderer){
            currentCacheCount--;
            if(currentCacheCount == 0) {
                CacheLayer.sharedRenderer.dispose();
                CacheLayer.sharedRenderer = null;
            }
        }
        else{
            this.renderer.dispose();
        }
    }
}
