package com.thommil.libgdx.runtime.scene;

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
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    void step(long lastStepDuration);
}
