package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.graphics.Renderable;

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
    protected final List<Renderable> renderables;

    /**
     * Current camera used by this layer
     */
    protected OrthographicCamera camera;

    /**
     * Default constructor
     */
    @SuppressWarnings("all")
    public Layer() {
        this.renderables = new ArrayList<Renderable>();
    }

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
    public void addRenderable(final Renderable renderable){
        this.renderables.add(renderable);
    }

    /**
     * Remove a renderable from the layer
     *
     * @param renderable The renderable to remove
     */
    public void removeRenderable(final Renderable renderable){
        this.renderables.remove(renderable);
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    public abstract void render(float deltaTime);

    /**
     * Shows/enables a layer
     */
    public void show(){
        this.onShow();
        this.visible = true;
    }

    /**
     * Called when render is enabled/showed for subclasses
     */
    protected abstract void onShow();

    /**
     * Hides/diables a layer
     */
    public void hide(){
        this.visible = false;
        this.onHide();
    }

    /**
     * Called when render is disabled/hidden for subclasses
     */
    protected abstract void onHide();

    /**
     * Indicates if layer is currently visible/enabled
     */
    public boolean isVisible() {
        return visible;
    }
}
