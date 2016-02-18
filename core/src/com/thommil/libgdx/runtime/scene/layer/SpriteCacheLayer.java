package com.thommil.libgdx.runtime.scene.layer;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.GameRuntimeException;
import com.thommil.libgdx.runtime.graphics.cache.SpriteCache;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;

/**
 * Basic Sprite layer using BasicSpriteBatch as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class SpriteCacheLayer extends Layer{

    private static int maxSprites = 1000;
    private static int currentCacheId = 0;
    private static SpriteCache renderer;
    private int cacheId;

    public static void setMaxSprites(final int maxSprites){
        if(SpriteCacheLayer.renderer != null){
            throw new GameRuntimeException("Maximum Sprites size must be set before creating a new SpriteCacheLayer");
        }
        SpriteCacheLayer.maxSprites = maxSprites;
    }

    public SpriteCacheLayer() {
        if(SpriteCacheLayer.renderer == null){
            SpriteCacheLayer.renderer = new SpriteCache(SpriteCacheLayer.maxSprites);
        }
        this.cacheId = currentCacheId++;
    }

    /**
     *  Cals this method to begin cache for underlying SpriteCache
     */
    public void beginCache(){
        SpriteCacheLayer.renderer.beginCache();
    }

    /**
     * Add a renderable to the layer
     *
     * @param renderable The renderable to add
     */
    @Override
    public void addRenderable(Renderable renderable) {
        if(renderable instanceof StaticActor){
            SpriteCacheLayer.renderer.add((StaticActor)renderable);
        }
        else if(renderable instanceof SpriteActor){
            SpriteCacheLayer.renderer.add((SpriteActor)renderable);
        }
    }

    /**
     *  Cals this method to begin cache for underlying SpriteCache
     */
    public void endCache(){
        SpriteCacheLayer.renderer.endCache();
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
        //NOP
    }

    @Override
    public void render(float deltaTime) {
        SpriteCacheLayer.renderer.begin(this.camera.combined);
        SpriteCacheLayer.renderer.draw(this.cacheId);
        SpriteCacheLayer.renderer.end();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
