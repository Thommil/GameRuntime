package com.thommil.libgdx.runtime.actor.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.actor.Actor;

/**
 * Rigid body actor without graphics
 *
 * @author thommil on 03/02/16.
 */
public abstract class HeadlessBodyActor extends Actor implements RigidBody {

    /**
     * The bound body
     */
    public Body body;

    /**
     * Default constructor
     *
     * @param id The ID of the Actor in the scene
     */
    public HeadlessBodyActor(final int id){
        super(id);
    }

    /**
     * Gets the definition of Collidable.
     *
     * @return definition The collidable definition (settings)
     */
    @Override
    public BodyDef getDefinition() {
        return new BodyDef();
    }

    /**
     * Set body instance of the Collidable
     *
     * @param body The Body instance
     */
    @Override
    public void setBody(Body body) {
        this.body = body;
        this.body.setUserData(this);
    }

    /**
     * Get the collidable body
     */
    @Override
    public Body getBody() {
        return this.body;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose(){
        this.body.getWorld().destroyBody(this.body);
    }
}
