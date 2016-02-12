package com.thommil.libgdx.runtime.scene.layer;

import com.thommil.libgdx.runtime.graphics.BasicBatch;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.Layer;

/**
 * Basic Sprite layer using BasicSpriteBatch as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class BasicBatchLayer extends Layer{

    final BasicBatch renderer;

    public BasicBatchLayer(final int maxSprites) {
        this.renderer = new BasicBatch(maxSprites);
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
    protected void onResize() {
        //NOP
    }

    @Override
    public void render(float deltaTime) {
        renderer.setProjectionMatrix(this.camera.combined);
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
