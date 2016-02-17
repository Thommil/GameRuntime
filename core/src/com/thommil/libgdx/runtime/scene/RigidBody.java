package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.List;

/**
 * Defines a physics actor with a rigid body
 *
 * Created by thommil on 12/02/16.
 */
public interface RigidBody extends Collidable<BodyDef,Body>{

    /**
     * Gets the Shapes of the Collidable
     */
    List<Shape> getShapes();

    /**
     * Gets the density to the RigidBody
     */
    float getDensity();

    /**
     * Gets the friction to the RigidBody
     */
    float getFriction();

    /**
     * Gets the restitution to the RigidBody
     */
    float getRestitution();

}
