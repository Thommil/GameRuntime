package com.thommil.libgdx.runtime.test.input.dynamic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.scene.Layer;

/**
 * Created by thommil on 03/02/16.
 */
public class PhysicsLayer extends Layer{

    SpriteBatch spriteBatch = new SpriteBatch();

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void render(float deltaTime) {
        //Gdx.app.debug("BasicLayer","render()");
        spriteBatch.setProjectionMatrix(this.camera.combined);
        spriteBatch.begin();
        for(Renderable renderable : this.renderables){
            renderable.render(deltaTime,spriteBatch);
        }
        spriteBatch.end();
        //Gdx.app.debug("","FPS : " + Gdx.graphics.getFramesPerSecond());
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}