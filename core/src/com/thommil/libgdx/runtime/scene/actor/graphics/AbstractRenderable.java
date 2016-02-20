package com.thommil.libgdx.runtime.scene.actor.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Empty actor used to listen for render calls and implements some game logic
 *
 * Created by tomtom on 20/02/16.
 */
public abstract class AbstractRenderable extends Actor implements Renderable<Batch> {

    protected int layer = 0;

    /**
     * Default constructor
     *
     * @param id Actor ID in the scene
     * @param layer Renderable layer in the scene
     */
    public AbstractRenderable(int id, int layer) {
        super(id);
        this.layer = layer;
    }

    /**
     * Returns the layer of the Renderable
     *
     * @return The layer of the Renderable
     */
    @Override
    public int getLayer() {
        return this.layer;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, Batch renderer) {
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
