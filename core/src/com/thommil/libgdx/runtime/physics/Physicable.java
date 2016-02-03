package com.thommil.libgdx.runtime.physics;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Defines an element which can interact with physic world
 *
 * Created by thommil on 03/02/16.
 */
public interface Physicable {

    /**
     * Get the body bound to the element
     *
     * @return A body instance
     */
    Body getBody();
}
