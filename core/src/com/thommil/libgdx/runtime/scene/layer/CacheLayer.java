package com.thommil.libgdx.runtime.scene.layer;

import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.graphics.renderer.cache.CacheRenderer;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;

/**
 * Basic Sprite layer using BasicSpriteBatch as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class CacheLayer extends Layer{

    private static int size = 1000;
    private static int currentCacheId = 0;
    private static CacheRenderer renderer;
    private int cacheId;

    /**
     * Set global cache size
     *
     * @param size The maximum size of the global cache shared among layers
     */
    public static void setSize(final int size){
        if(CacheLayer.renderer != null){
            throw new GameRuntimeException("CacheLayer size must be set before creating a new CacheLayer");
        }
        CacheLayer.size = size;
    }

    /**
     * Default constructor
     *
     * @param initialCapacity The initial capacity of the layer
     */
    public CacheLayer(final int initialCapacity) {
        super(initialCapacity);
        if(CacheLayer.renderer == null){
            CacheLayer.renderer = new CacheRenderer(CacheLayer.size);
        }
        this.cacheId = currentCacheId++;
    }

    /**
     *  Cals this method to begin cache for underlying CacheRenderer
     */
    public void beginCache(){
        CacheLayer.renderer.beginCache();
    }

    /**
     *  Cals this method to begin cache for underlying CacheRenderer
     */
    public void endCache(){
        CacheLayer.renderer.endCache();
    }

    /**
     * Add a renderable to the layer
     *
     * @param renderable The renderable to add
     */
    @Override
    public void add(Renderable renderable) {
        if(renderable instanceof StaticActor){
            CacheLayer.renderer.add((StaticActor)renderable);
        }
        else if(renderable instanceof SpriteActor){
            CacheLayer.renderer.add((SpriteActor)renderable);
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
            CacheLayer.renderer.setCombinedMatrix(this.camera.combined);
            CacheLayer.renderer.begin();
            CacheLayer.renderer.draw(this.cacheId);
            CacheLayer.renderer.end();
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
        if(CacheLayer.renderer != null) {
            CacheLayer.renderer.dispose();
            CacheLayer.renderer = null;
        }
        CacheLayer.size = 1000;
        CacheLayer.currentCacheId = 0;
    }
}
