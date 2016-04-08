package com.thommil.libgdx.runtime.layer;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.renderer.cache.SpriteCacheRenderer;
import com.thommil.libgdx.runtime.actor.graphics.Renderable;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.actor.graphics.StaticActor;

/**
 * Basic Sprite layer using shared/custom BasicSpriteBatch as Renderer
 *
 * @author thommil on 03/02/16.
 */
public class CacheLayer extends Layer{

    protected static int size = 1000;
    protected static int currentConsumersCount = 0;
    protected static SpriteCacheRenderer sharedRenderer;
    protected int cacheId = -1;

    protected final SpriteCacheRenderer renderer;

    /**
     * Set global cache size in sprites count (maximum size !!!)
     *
     * @param size The maximum size of the global cache shared among layers
     */
    public static void setGlobalSize(final int size){
        if(CacheLayer.sharedRenderer != null){
            throw new GameRuntimeException("Global cache size must be set before creating a new shared CacheLayer");
        }
        CacheLayer.size = size;
    }

    /**
     * Default constructor using the default shared cache
     *
     * @param viewport The viewport bound to the layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     */
    public CacheLayer(final Viewport viewport, final int initialCapacity) {
        super(viewport, initialCapacity);
        if(CacheLayer.sharedRenderer == null){
            CacheLayer.sharedRenderer = new SpriteCacheRenderer(CacheLayer.size);
        }
        this.renderer = CacheLayer.sharedRenderer;
        CacheLayer.currentConsumersCount++;
    }

    /**
     * Custom cache renderer constructor
     *
     * @param viewport The viewport bound to the layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     * @param renderer The custom renderer to use
     */
    public CacheLayer(final Viewport viewport, final int initialCapacity, final SpriteCacheRenderer renderer) {
        super(viewport, initialCapacity);
        this.renderer = renderer;
    }

    /**
     * Add an renderable to the layer
     */
    @Override
    public void addActor(Actor actor) {
        if(!this.isHidden()) throw new GameRuntimeException("Actors can't be added in CacheLayer in visible state");
        super.addActor(actor);
    }

    /**
     * Remove a renderable from the layer
     */
    @Override
    public void removeActor(Actor actor) {
        if(!this.isHidden()) throw new GameRuntimeException("Actors can't be removed from CacheLayer in visible state");
        super.removeActor(actor);
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
            this.renderer.draw(this.cacheId);
            this.renderer.end();
        }
    }

    /**
     * Called when layer is showed for subclasses
     */
    @Override
    protected void onShow() {
        if(this.cacheId < 0){
            this.renderer.beginCache();
            for(Renderable renderable : this.renderables){
                if(renderable instanceof StaticActor){
                    this.renderer.add((StaticActor)renderable);
                }
                else if(renderable instanceof SpriteActor){
                    this.renderer.add((SpriteActor)renderable);
                }
            }
            this.cacheId = this.renderer.endCache();
        }
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
            CacheLayer.currentConsumersCount--;
            if(CacheLayer.currentConsumersCount == 0) {
                CacheLayer.sharedRenderer.dispose();
                CacheLayer.sharedRenderer = null;
            }
            else{
                this.renderer.beginCache(this.cacheId);
                this.renderer.endCache();
            }
        }
    }
}
