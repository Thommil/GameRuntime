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
     * Components of the actor in physics/step phase
     * <br/><br/>
     * These components are modified by the physics engine and
     * should be accessed in write mode only in physics phase
     * only if really needed.
     */
    public float[] _components = new float[3];

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
}
