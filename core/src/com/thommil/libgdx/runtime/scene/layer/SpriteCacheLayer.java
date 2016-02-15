package com.thommil.libgdx.runtime.scene.layer;

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

    final SpriteCache renderer;
    boolean cacheEnded = false;
    int cacheId;

    public SpriteCacheLayer(final int maxSprites) {
        this.renderer = new SpriteCache(maxSprites);
        this.renderer.beginCache();
    }

    /**
     * Add a renderable to the layer
     *
     * @param renderable The renderable to add
     */
    @Override
    public void addRenderable(Renderable renderable) {
        if(cacheEnded) throw new GameRuntimeException("Cache is closed, can add renderables after show()");

        if(renderable instanceof StaticActor){
            this.renderer.add((StaticActor)renderable);
        }
        else if(renderable instanceof SpriteActor){
            this.renderer.add((SpriteActor)renderable);
        }
    }

    @Override
    public void onShow() {
        if(!cacheEnded){
            cacheId = this.renderer.endCache();
            cacheEnded = true;
        }
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
        renderer.begin(this.camera.combined);
        renderer.draw(cacheId);
        renderer.end();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
