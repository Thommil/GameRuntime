package com.thommil.libgdx.runtime.scene.layer;

import com.thommil.libgdx.runtime.scene.Layer;

/**
 * Empty layer used to listen for UI events and implements some game logic
 *
 * Created by thommil on 18/02/16.
 */
public abstract class LogicLayer extends Layer {

    /**
     * Default constructor
     */
    public LogicLayer() {
        super(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * Constructor with specific capacity
     *
     * @param initialCapacity The initial capacity of the layer
     */
    public LogicLayer(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Render complete layer
     *
     * @param deltaTime Time since last call
     */
    @Override
    public void render(float deltaTime) {
        //NOP
    }

    /**
     * Called when layer is showed for subclasses
     */
    @Override
    protected void onShow() {
        //NOP
    }

    /**
     * Called when layer is hidden
     */
    @Override
    protected void onHide() {
        //NOP
    }

    /**
     * Called when layer is resized
     *
     * @param width
     * @param height
     */
    @Override
    protected void onResize(int width, int height) {
        //NOP
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        //NOP
    }
}
