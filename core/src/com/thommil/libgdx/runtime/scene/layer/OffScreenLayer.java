package com.thommil.libgdx.runtime.scene.layer;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.thommil.libgdx.runtime.graphics.renderer.OffScreenRenderer;
import com.thommil.libgdx.runtime.scene.Layer;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Layer decorator for offscreen buffering purpose
 *
 * @author thommil on 03/02/16.
 */
public class OffScreenLayer<T extends Layer> extends Layer {

    final protected OffScreenRenderer offScreenRenderer;
    final protected Layer decoratedLayer;

    public OffScreenLayer(final T decoratedLayer, final OffScreenRenderer offScreenRenderer) {
        super(1);
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
        if(!this.hidden) {
            this.offScreenRenderer.setCombinedMatrix(this.camera.combined);
            this.offScreenRenderer.begin();
            this.decoratedLayer.render(deltaTime);
            this.offScreenRenderer.draw();
            this.offScreenRenderer.end();
        }
    }

    /**
     * Set the current camera
     *
     * @param camera
     */
    @Override
    public void setCamera(OrthographicCamera camera) {
        super.setCamera(camera);
        this.decoratedLayer.setCamera(camera);
    }

    /**
     * Add a renderable to the layer
     *
     * @param renderable The renderable to add
     */
    @Override
    public void add(Renderable renderable) {
        this.decoratedLayer.add(renderable);
    }

    /**
     * Gets the list of renderables
     *
     * @return The list of renderables
     */
    @Override
    public Renderable[] getRenderables() {
        return this.decoratedLayer.getRenderables();
    }

    /**
     * Remove a renderable from the layer
     *
     * @param renderable The renderable to remove
     */
    @Override
    public void remove(Renderable renderable) {
        this.decoratedLayer.remove(renderable);
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
        this.decoratedLayer.resize(width, height);
        this.offScreenRenderer.onResize(this.camera.viewportWidth, this.camera.viewportHeight, width, height);
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
