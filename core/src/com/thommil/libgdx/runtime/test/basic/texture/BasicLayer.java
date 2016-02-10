package com.thommil.libgdx.runtime.test.basic.texture;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.Layer;

/**
 * Created by thommil on 03/02/16.
 */
public class BasicLayer extends Layer{

    SpriteBatch spriteBatch = new SpriteBatch();

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
        spriteBatch.setProjectionMatrix(this.camera.combined);
        spriteBatch.begin();
        for(Renderable renderable : this.renderables){
            renderable.render(deltaTime,spriteBatch);
        }
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
