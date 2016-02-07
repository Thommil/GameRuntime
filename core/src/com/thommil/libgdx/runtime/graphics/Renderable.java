package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.thommil.libgdx.runtime.scene.Actor;

/**
 * Defines a renderable element
 *
 * Created by thommil on 01/02/16.
 */
public interface Renderable extends Actor {

    /**
     * Returns the layer of the Renderable
     *
     * @return The layer of the Renderable
     */
    int getLayer();

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer The batch renderer to use
     */
    void render(float deltaTime, Batch renderer);

}
