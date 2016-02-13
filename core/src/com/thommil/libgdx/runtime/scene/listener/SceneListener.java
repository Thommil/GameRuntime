package com.thommil.libgdx.runtime.scene.listener;

import com.badlogic.gdx.physics.box2d.ContactListener;

/**
 * Created by tomtom on 05/02/16.
 */
public interface SceneListener extends ContactListener{

    /**
     * Called before each worl step with the duration of the last step processing
     *
     * @param lastDuration The duration of the last processing for QoS purpose
     */
    void onStep(final long lastDuration);

    /**
     * Called before each rendering
     *
     * @param deltaTime The time ellapsed since last rendering
     */
    void onRender(final float deltaTime);
}
