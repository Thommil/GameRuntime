package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Stepable;

/**
 * Empty actor used to listen for steps and implements some game logic
 *
 * Created by thommil on 18/02/16.
 */
public abstract class LogicActor implements Actor, Stepable{

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return MathUtils.random(0x7ffffffe);
    }

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    @Override
    public void step(long lastStepDuration){
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
