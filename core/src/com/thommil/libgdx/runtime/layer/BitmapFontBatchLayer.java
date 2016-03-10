package com.thommil.libgdx.runtime.layer;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.thommil.libgdx.runtime.actor.graphics.Renderable;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;

/**
 * Specific layer for BitmapFont to allow correct rendering using a ScreenViewport.
 *
 * Any coordinate in this layer is relative ti the current screen size.
 *
 * @autor Thommil on 3/4/16.
 */
public class BitmapFontBatchLayer extends SpriteBatchLayer {

    /**
     * Default constructor using the default shared renderer
     *
     * @param initialCapacity The initial capacity of the layer (number of actors)
     */
    public BitmapFontBatchLayer(int initialCapacity) {
        super(new ScreenViewport(), initialCapacity);
    }

    /**
     * Constructor with custom renderer
     *
     * @param initialCapacity The initial capacity of the layer (number of actors)
     * @param customRenderer  The custom renderer to use
     */
    public BitmapFontBatchLayer(int initialCapacity, SpriteBatchRenderer customRenderer) {
        super(new ScreenViewport(), initialCapacity, customRenderer);
    }

    /**
     * Called when layer is resized
     */
    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        this.viewport.update(width, height);
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        if(!this.isHidden()) {
            renderer.setCombinedMatrix(this.viewport.getCamera().combined);
            this.renderer.begin();
            for (Renderable renderable : this.renderables) {
                renderable.render(deltaTime, this.renderer);
            }
            this.renderer.end();
        }
    }
}
