package com.thommil.libgdx.runtime.actor.physics;

/**
 * Defines a physics object with a body
 *
 * @author thommil on 03/02/16.
 */
public interface Collidable<Definition, Body>{

    /**
     * Gets the definition of Collidable.
     *
     * @return definition The collidable definition (settings)
     */
    Definition getDefinition();

    /**
     * Called at body creation, implementations can add
     * settings, behaviour and logic from here.
     *
     * @param body The collidable body instance
     */
    void setBody(final Body body);

    /**
     * Get the collidable body
     */
    Body getBody();
}
