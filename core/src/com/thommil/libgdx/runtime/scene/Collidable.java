package com.thommil.libgdx.runtime.scene;

/**
 * Created by tomtom on 14/02/16.
 */
public interface Collidable<Definition, Body>{

    /**
     * Sets the definition of Collidable, implementations
     * should configure Collidable settings in the passed
     * definition.
     *
     * @param definition The collidable definition (settings)
     */
    void setDefinition(final Definition definition);

    /**
     * Called at body creation, implementations can add
     * settings, behaviour and logic from here.
     *
     * @param body The collidable body instance
     */
    void setBody(final Body body);
}
