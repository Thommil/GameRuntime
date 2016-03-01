package com.thommil.libgdx.runtime.runtime.layer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.runtime.actor.graphics.Renderable;

/**
 * Represents a rendering layer (mainly for batch purpose)
 *
 * @author thommil on 03/02/16.
 */
public abstract class Layer implements Disposable {

    /**
     * Indicates visible/enabled state
     */
    protected boolean hidden = true;

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
     * Shows a layer
     */
    public final void show(){
        this.hidden = false;
        this.onShow();
    }

    /**
     * Hides a layer
     */
    public final void hide(){
        this.hidden = true;
        this.onHide();
    }

    /**
     * Resizes a layer
     */
    public final void resize(int width, int height){
        this.onResize(width, height);
    }

    /**
     * Indicates if layer is currently visible/enabled
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    public abstract void render(float deltaTime);

    /**
     * Called when layer is resized
     */
    protected abstract void onResize(int width, int height);

    /**
     * Called when layer is showed for subclasses. Fade in can be done
     * by setting hidden to true here and adjusting rendering alpha.
     */
    protected abstract void onShow();

    /**
     * Called when layer is hidden for subclasses. Fade out can be done
     * by setting hidden to false here and adjusting rendering alpha.
     */
    protected abstract void onHide();

}