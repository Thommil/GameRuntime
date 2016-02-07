package com.thommil.libgdx.runtime.test.render.cache;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.scene.Layer;

/**
 * Created by thommil on 03/02/16.
 */
public class CacheLayer extends Layer{

    SpriteCache spriteCache = new SpriteCache();

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void render(float deltaTime) {
        //Gdx.app.debug("BasicLayer","render()");
        spriteCache.setProjectionMatrix(this.camera.combined);
        spriteCache.begin();
        for(Renderable renderable : this.renderables){
            //renderable.render(deltaTime,spriteCache);
        }
        spriteCache.end();
        //Gdx.app.debug("","FPS : " + Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void dispose() {
        spriteCache.dispose();
    }
}
