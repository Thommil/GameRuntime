package com.thommil.libgdx.runtime.runtime.actor.graphics;

import com.thommil.libgdx.runtime.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.renderer.Renderer;

/**
 * Empty actor used to listen for render calls and implements some game logic
 *
 * @author thommil on 03/02/16.
 */
public abstract class AbstractRenderable extends Actor implements Renderable<Renderer> {

    /**
     * Default constructor
     *
     * @param id Actor ID in the scene
     */
    public AbstractRenderable(int id) {
        super(id);
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, Renderer renderer) {
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
