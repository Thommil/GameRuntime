package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Defines an actor which can interact with physic world
 *
 * Created by thommil on 03/02/16.
 */
public abstract class PhysicsActor extends Actor{

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
    protected abstract Body buildBody(final World world);

    protected abstract void step(long lastStepDuration);
}
