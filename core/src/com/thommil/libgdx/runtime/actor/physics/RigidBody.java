package com.thommil.libgdx.runtime.actor.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

/**
 * Defines a physics actor with a rigid body
 *
 * @author thommil on 03/02/16.
 */
public interface RigidBody extends Collidable<BodyDef,Body> {

    /**
     * Gets the definition of Collidable.
     *
     * @return definition The collidable definition (settings)
     */
    @Override
    BodyDef getDefinition();

    /**
     * Gets the fixtures definition of the Collidable
     */
    Array<FixtureDef> getFixturesDefinition();

    /**
     * Called at body creation, implementations can add
     * settings, behaviour and logic from here.
     *
     * @param body The collidable body instance
     */
    @Override
    void setBody(final Body body);

    /**
     * Get the collidable body
     */
    @Override
    Body getBody();
}
