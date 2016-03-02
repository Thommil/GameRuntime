package com.thommil.libgdx.runtime.actor.graphics;

import com.thommil.libgdx.runtime.graphics.renderer.BatchRenderer;

/**
 * Defines a renderable element using T as renderer
 *
 * @author thommil on 03/02/16.
 */
public interface Renderable<T extends BatchRenderer>{

    /**
     * Render the element on current viewport
     *
     * @param deltaTime The delta time since last call in seconds
     * @param renderer The renderer to use in current layer
     */
    void render(float deltaTime, T renderer);

}
