package com.thommil.libgdx.runtime.layer;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.actor.graphics.Renderable;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;

/**
 * Specific layer for BitmapFont to allow correct rendering on none SCREEN viewports.
 * If using SCREEN viewport, use SpriteBatchLayer instead.
 *
 * @autor Thommil on 3/4/16.
 */
public class BitmapFontBatchLayer extends SpriteBatchLayer {

    protected float scale;
    protected final Matrix4 tmpCombined = new Matrix4();

    /**
     * Default constructor using the default shared renderer
     *
     * @param viewport        The viewport bound to the layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     */
    public BitmapFontBatchLayer(Viewport viewport, int initialCapacity) {
        super(viewport, initialCapacity);
    }

    /**
     * Constructor with custom renderer
     *
     * @param viewport        The viewport bound to the layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     * @param customRenderer  The custom renderer to use
     */
    public BitmapFontBatchLayer(Viewport viewport, int initialCapacity, SpriteBatchRenderer customRenderer) {
        super(viewport, initialCapacity, customRenderer);
    }

    /**
     * Called when layer is resized
     */
    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        this.scale = Math.min(this.viewport.getWorldWidth() / width, this.viewport.getWorldHeight() / height);
    }

    /**
     * Get the current scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        if(!this.isHidden()) {
            this.tmpCombined.set(this.viewport.getCamera().combined);
            this.tmpCombined.scale(this.scale, this.scale, 1.0f);
            renderer.setCombinedMatrix(this.tmpCombined);
            this.renderer.begin();
            for (Renderable renderable : this.renderables) {
                renderable.render(deltaTime, this.renderer);
            }
            this.renderer.end();
        }
    }
}
