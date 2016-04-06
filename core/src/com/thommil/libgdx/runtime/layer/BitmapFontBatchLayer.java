package com.thommil.libgdx.runtime.layer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.actor.graphics.Renderable;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;

/**
 * Specific layer for BitmapFont to allow correct rendering using a ScreenViewport.
 *
 * Any coordinate in this layer is relative ti the current screen size.
 *
 * @author Thommil on 3/4/16.
 */
public class BitmapFontBatchLayer extends SpriteBatchLayer {

    /**
     * Inner screen viewport used to render fonts
     */
    private final ScreenViewport screenViewport = new ScreenViewport();

    /**
     * Default constructor using the default shared renderer
     *
     * @param viewport The viewport used arounf layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     */
    public BitmapFontBatchLayer(final Viewport viewport, int initialCapacity) {
        super(viewport, initialCapacity);
    }

    /**
     * Constructor with custom renderer
     * @param viewport The viewport used arounf layer
     * @param initialCapacity The initial capacity of the layer (number of actors)
     * @param customRenderer  The custom renderer to use
     */
    public BitmapFontBatchLayer(final Viewport viewport, int initialCapacity, SpriteBatchRenderer customRenderer) {
        super(viewport, initialCapacity, customRenderer);
    }

    /**
     * Called when layer is resized
     */
    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        this.screenViewport.update(width, height);
        this.viewport.apply();
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        if(!this.isHidden()) {
            this.screenViewport.apply();
            this.renderer.setCombinedMatrix(this.screenViewport.getCamera().combined);
            this.renderer.begin();
            for (final Renderable renderable : this.renderables) {
                renderable.render(deltaTime, this.renderer);
            }
            this.renderer.end();
            this.viewport.apply();
        }
    }
}
