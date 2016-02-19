package com.thommil.libgdx.runtime.scene.layer;

import com.thommil.libgdx.runtime.graphics.batch.SpriteBatch;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.Layer;

/**
 * Basic Sprite layer using BasicSpriteBatch as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class SpriteBatchLayer extends Layer{

    final protected SpriteBatch renderer;

    public SpriteBatchLayer(final int maxSprites) {
        this.renderer = new SpriteBatch(maxSprites);
    }

    public SpriteBatchLayer(final SpriteBatch customRenderer) {
        this.renderer = customRenderer;
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
        renderer.setCombinedMatrix(this.camera.combined);
        renderer.begin();
        for(Renderable renderable : this.renderables){
            renderable.render(deltaTime,renderer);
        }
        renderer.end();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
