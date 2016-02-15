package com.thommil.libgdx.runtime.scene.layer;

import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.graphics.batch.SoftBodyBatch;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * SoftBody layer using SoftBodyBatch as renderer
 *
 * Created by thommil on 03/02/16.
 */
public class SoftBodyBatchLayer extends Layer{

    final SoftBodyBatch renderer;

    public SoftBodyBatchLayer(final int maxSprites) {
        this.renderer = new SoftBodyBatch(maxSprites);
    }

    protected float particlesScale;

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
        this.particlesScale = Math.min(width / this.camera.viewportWidth, height / this.camera.viewportHeight);
    }

    @Override
    public void render(float deltaTime) {
        renderer.begin(this.camera.combined, this.particlesScale);
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
