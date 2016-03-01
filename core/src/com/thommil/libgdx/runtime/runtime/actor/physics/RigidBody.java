package com.thommil.libgdx.runtime.runtime.actor.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.runtime.actor.physics.Collidable;

import java.util.List;

/**
 * Defines a physics actor with a rigid body
 *
 * @author thommil on 03/02/16.
 */
public interface RigidBody extends Collidable<BodyDef,Body> {

    /**
     * Gets the Shapes of the Collidable
     */
    List<Shape> getShapes();

    /**
     * Gets the density to the shapes
     */
    float getDensity();

    /**
     * Sets the definition of Collidable, implementations
     * should configure Collidable settings in the passed
     * definition.
     *
     * @param bodyDef The collidable definition (settings)
     */
    @Override
    void setDefinition(final BodyDef bodyDef);

    /**
     * Called at body creation, implementations can add
     * settings, behaviour and logic from here.
     *
     * @param body The collidable body instance
     */
    @Override
    void setBody(final Body body);
}
