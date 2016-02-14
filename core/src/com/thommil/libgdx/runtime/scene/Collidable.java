package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by tomtom on 14/02/16.
 */
public interface Collidable<Definition, Body> extends Actor{

    /**
     * Gets the definition of Collidable
     */
    Definition getDefinition();

    /**
     * Set body instance of the Collidable
     */
    void setBody(Body body);

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    void step(long lastStepDuration);
}
