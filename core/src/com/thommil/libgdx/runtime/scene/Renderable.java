package com.thommil.libgdx.runtime.scene;

/**
 * Defines a renderable element using T as renderer
 *
 * Created by thommil on 01/02/16.
 */
public interface Renderable<T extends Renderer>{

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
     * @param renderer The renderer to use in current layer
     */
    void render(float deltaTime, T renderer);

}
