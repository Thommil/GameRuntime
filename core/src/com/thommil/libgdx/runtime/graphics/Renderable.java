package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Defines a renderable element
 *
 * Created by thommil on 01/02/16.
 */
public interface Renderable {

    /**
     * Render the element on current viewport
     *
     * @param deltaTime The delta time since last call
     */
    void render(float deltaTime);

}
