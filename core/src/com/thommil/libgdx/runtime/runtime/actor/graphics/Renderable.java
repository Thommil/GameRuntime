package com.thommil.libgdx.runtime.runtime.actor.graphics;

import com.thommil.libgdx.runtime.graphics.renderer.BatchRenderer;

/**
 * Defines a renderable element using T as renderer
 *
 * @author thommil on 03/02/16.
 */
public interface Renderable<T extends BatchRenderer>{

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer The renderer to use in current layer
     */
    void render(float deltaTime, T renderer);

}
