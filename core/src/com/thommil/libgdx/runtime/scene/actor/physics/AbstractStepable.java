package com.thommil.libgdx.runtime.scene.actor.physics;

import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Stepable;

/**
 * Empty actor used to listen for steps calls and implements some game logic
 *
 * Created by thommil on 18/02/16.
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
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    @Override
    public void step(long lastStepDuration) {
        //NOP
    }
}
