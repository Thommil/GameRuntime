package com.thommil.libgdx.runtime.actor.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;

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
     * Gets the definition of Collidable.
     *
     * @return definition The collidable definition (settings)
     */
    @Override
    BodyDef getDefinition();

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
