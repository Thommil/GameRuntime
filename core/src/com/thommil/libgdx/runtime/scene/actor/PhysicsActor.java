package com.thommil.libgdx.runtime.scene.actor;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.Actor;

/**
 * Defines an SpriteActor actor which can interact with physic world
 *
 * Created by thommil on 03/02/16.
 */
public abstract class PhysicsActor extends SpriteActor {

    /**
     * The bound body
     */
    public Body body;

    /**
     * System method used by Scene.
     */
    public void _init(final World world){
        this.body = this.buildBody(world);
        this.body.setUserData(this);
    }

    /**
     * Subclasses must implement this method to build the body
     * of this instance. The body is automatically associated to
     * the actor in the _init() method and can't be overriden.
     */
    public abstract Body buildBody(final World world);

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    public abstract void step(long lastStepDuration);
}
