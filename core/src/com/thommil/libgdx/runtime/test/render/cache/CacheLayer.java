package com.thommil.libgdx.runtime.test.render.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.scene.Layer;

/**
 * //TODO Extends/rewrite SpriteCache to handle any geometry
 *
 * Created by thommil on 03/02/16.
 */
public class CacheLayer extends Layer{

    SpriteCache spriteCache = new SpriteCache(10,true);
    Texture texture;

    final int cacheId;

    public CacheLayer(){
        this.spriteCache.beginCache();
        this.texture = new Texture(Gdx.files.internal("test.png"));
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        float[] vertices = new float[]
                {
                        -10f, -8f, Color.toFloatBits(1f,1f,1f,1f), 0f , 0f,
                        -10f, -10f, Color.toFloatBits(1f,1f,1f,1f), 0f , 0.1f,
                        0f, -10f, Color.toFloatBits(1f,1f,1f,1f), 1f , 0.1f,
                        0f, -10f, Color.toFloatBits(1f,1f,1f,1f), 1f , 0.1f
                };

        this.spriteCache.add(texture, vertices,0,vertices.length);
            vertices = new float[]
                {
                        0f, -10f, Color.toFloatBits(1f,1f,1f,1f), 0f , 0.1f,
                        10f, -10f, Color.toFloatBits(1f,1f,1f,1f), 1f , 0.1f,
                        10f, -8f, Color.toFloatBits(1f,1f,1f,1f), 1f , 0f,
                        10f, -8f, Color.toFloatBits(1f,1f,1f,1f), 1f , 0f

                };
        this.spriteCache.add(texture, vertices,0,vertices.length);
        cacheId = this.spriteCache.endCache();
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    protected void onResize() {

    }

    @Override
    public void render(float deltaTime) {
        //Gdx.app.debug("CacheLayer","render()");
        spriteCache.setProjectionMatrix(this.camera.combined);
        spriteCache.begin();
        spriteCache.draw(cacheId);
        spriteCache.end();
        //Gdx.app.debug("","FPS : " + Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void dispose() {
        spriteCache.dispose();
        this.texture.dispose();
    }
}
