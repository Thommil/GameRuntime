package com.thommil.libgdx.runtime.actor.physics;

import com.thommil.libgdx.runtime.actor.Actor;

/**
 * Empty actor used to listen for steps calls and implements some game logic
 *
 * @author thommil on 03/02/16.
 */
public abstract class AbstractStepable extends Actor implements Stepable{

    /**
     * Default constructor
     *
     * @param id The ID of the actor
     */
    public AbstractStepable(int id) {
        super(id);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        //NOP
    }

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param deltaTime the time span between the current step and the last step in seconds.
     */
    @Override
    public void step(float deltaTime) {
        //NOP
    }
}