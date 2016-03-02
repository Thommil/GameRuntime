package com.thommil.libgdx.runtime.layer;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.renderer.buffer.OffScreenRenderer;

/**
 * Layer decorator for offscreen buffering purpose
 *
 * @author thommil on 03/02/16.
 */
public class OffScreenLayer<T extends Layer> extends Layer {

    final protected OffScreenRenderer offScreenRenderer;
    final protected Layer decoratedLayer;

    public OffScreenLayer(final Viewport viewport, final T decoratedLayer, final OffScreenRenderer offScreenRenderer) {
        super(viewport,1);
        this.decoratedLayer = decoratedLayer;
        this.offScreenRenderer = offScreenRenderer;
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        if(!this.isHidden()) {
            this.offScreenRenderer.setCombinedMatrix(this.viewport.getCamera().combined);
            this.offScreenRenderer.begin();
            this.decoratedLayer.render(deltaTime);
            this.offScreenRenderer.draw();
            this.offScreenRenderer.end();
        }
    }

    /**
     * Add an renderable to the layer
     *
     * @param actor
     */
    @Override
    public void addActor(Actor actor) {
        this.decoratedLayer.addActor(actor);
    }

    /**
     * Remove a renderable from the layer
     *
     * @param actor
     */
    @Override
    public void removeActor(Actor actor) {
        this.decoratedLayer.addActor(actor);
    }

    /**
     * Indicates if the layer is flagged as hidden
     *
     * @return Hidden state
     */
    @Override
    public boolean isHidden() {
        return this.decoratedLayer.isHidden();
    }

    /**
     * Step call
     *
     * @param deltaTime Time since last call since last call in seconds
     */
    @Override
    public void step(float deltaTime) {
        this.decoratedLayer.step(deltaTime);
    }

    /**
     * Called when layer is showed for subclasses
     */
    @Override
    protected void onShow() {
        this.decoratedLayer.show();
    }

    /**
     * Called when layer is hidden
     */
    @Override
    protected void onHide() {
        this.decoratedLayer.hide();
    }

    /**
     * Called when layer is resized
     *
     * @param width
     * @param height
     */
    @Override
    protected void onResize(int width, int height) {
        this.decoratedLayer.onResize(width, height);
        this.offScreenRenderer.onResize(this.viewport.getWorldWidth(), this.viewport.getWorldHeight(), width, height);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.offScreenRenderer.dispose();
        this.decoratedLayer.dispose();
    }
}
