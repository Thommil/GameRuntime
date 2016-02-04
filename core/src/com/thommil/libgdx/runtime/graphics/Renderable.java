package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Defines a renderable element
 *
 * Created by thommil on 01/02/16.
 */
public interface Renderable {

    /**
     * Returns the layer of the Renderable
     *
     * @return The layer of the Renderable
     */
    int getLayer();

    /**
     * Render the element on current viewport
     *
     * @param deltaTime The delta time since last call
     * @param renderer The batch renderer to use
     */
    void render(float deltaTime, Batch renderer);

}
