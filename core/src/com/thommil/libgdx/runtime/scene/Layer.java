package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
    protected final List<Renderable> renderables = new ArrayList<Renderable>();

    /**
     * Current camera used by this layer
     */
    protected OrthographicCamera camera;

    /**
     * Set the current camera
     */
    public void setCamera(OrthographicCamera camera){
        //Gdx.app.debug("Layer","setCamera()");
        this.camera = camera;
    }

    /**
     * Add a renderable to the layer
     *
     * @param renderable The renderable to add
     */
    public void addRenderable(final Renderable renderable){
        //Gdx.app.debug("Layer","addRenderable()");
        this.renderables.add(renderable);
    }

    /**
     * Gets the list of renderables
     *
     * @return The list of renderables
     */
    public List<Renderable> getRenderables(){
        // Gdx.app.debug("Layer","getRenderables()");
        return this.renderables;
    }

    /**
     * Remove a renderable from the layer
     *
     * @param renderable The renderable to remove
     */
    public void removeRenderable(final Renderable renderable){
        //Gdx.app.debug("Layer","removeRenderable()");
        this.renderables.remove(renderable);
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
        //Gdx.app.debug("Layer","show()");
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
        //Gdx.app.debug("Layer","hide()");
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
    protected abstract void onResize();

    /**
     * Indicates if layer is currently visible/enabled
     */
    public boolean isVisible() {
        return visible;
    }
}
