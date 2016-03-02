package com.thommil.libgdx.runtime.actor.physics;

/**
 * Defines an stepable element
 *
 * @author thommil on 03/02/16.
 */
public interface Stepable {

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param deltaTime the time span between the current step and the last step in seconds.
     */
    void step(float deltaTime);
}
