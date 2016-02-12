package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by tomtom on 12/02/16.
 */
public interface Collidable extends Actor{

    /**
     * Subclasses must implement this method to build the body
     * of this instance.
     */
    void buildBody(final World world);

    /**
     * @return The body of this instance
     */
    Body getBody();

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    void step(long lastStepDuration);
}
