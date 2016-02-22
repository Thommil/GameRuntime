package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rendering layer (mainly for batch purpose)
 *
 * Created by thommil on 03/02/16.
 */
public abstract class Layer implements Disposable {

    /**
     * Indicates visible/enabled state
     */
    private boolean visible = false;

    /**
     * Inner Renderable actors list
     */
    protected final Array<Renderable> renderables;

    /**
     * Default constructor
     *
     * @param initialCapacity The initial capacity of the layer
     */
    public Layer(final int initialCapacity) {
        this.renderables = new Array<Renderable>(false, initialCapacity);
    }

    /**
     * Current camera used by this layer
     */
    protected OrthographicCamera camera;

    /**
     * Set the current camera
     */
    public void setCamera(OrthographicCamera camera){
        this.camera = camera;
    }

    /**
     * Add a renderable to the layer
     *
     * @param renderable The renderable to add
     */
    public void add(final Renderable renderable){
        this.renderables.add(renderable);
    }

    /**
     * Gets the list of renderables
     *
     * @return The list of renderables
     */
    public Renderable[] getRenderables(){
        return this.renderables.items;
    }

    /**
     * Remove a renderable from the layer
     *
     * @param renderable The renderable to remove
     */
    public void remove(final Renderable renderable){
        this.renderables.removeValue(renderable, false);
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    public abstract void render(float deltaTime);

    /**
     * Shows a layer
     */
    public void show(){
        this.onShow();
        this.visible = true;
    }

    /**
     * Called when layer is showed for subclasses
     */
    protected abstract void onShow();

    /**
     * Hides a layer
     */
    public void hide(){
        this.visible = false;
        this.onHide();
    }

    /**
     * Called when layer is hidden
     */
    protected abstract void onHide();

    /**
     * Called when layer is resized
     */
    protected abstract void onResize(int width, int height);

    /**
     * Indicates if layer is currently visible/enabled
     */
    public boolean isVisible() {
        return visible;
    }
}
