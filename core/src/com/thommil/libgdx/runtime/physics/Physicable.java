package com.thommil.libgdx.runtime.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Defines an element which can interact with physic world
 *
 * Created by thommil on 03/02/16.
 */
public interface Physicable {

    /**
     * Static physics type
     */
    public static final int STATIC = BodyDef.BodyType.StaticBody.getValue();

    /**
     * Dynamic physics type
     */
    public static final int DYNAMIC = BodyDef.BodyType.DynamicBody.getValue();

    /**
     * Kinematic physics type
     */
    public static final int KINEMATIC = BodyDef.BodyType.KinematicBody.getValue();


    /**
     * Get the body type
     */
    int getBodyType();

    /**
     * Get the body bound to the element
     *
     * @return A body instance
     */
    Body getBody();
}
