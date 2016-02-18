package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.RigidBody;

import java.util.List;

/**
 * Simple sensor implementation
 *
 * Created by thommil on 13/02/16.
 */
public abstract class SensorActor implements RigidBody {

    protected final int id;

    /**
     * The bound body
     */
    public Body body;

    public SensorActor(final int id){
        this.id = id;
    }

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Gets the Shapes of the Collidable
     */
    @Override
    public abstract List<Shape> getShapes();

    /**
     * Gets the density to the RigidBody
     */
    @Override
    public float getDensity() {
        return 0.0f;
    }

    /**
     * Gets the friction to the RigidBody
     */
    @Override
    public float getFriction() {
        return 0.0f;
    }

    /**
     * Gets the restitution to the RigidBody
     */
    @Override
    public float getRestitution() {
        return 0.0f;
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        return bodyDef;
    }

    /**
     * Set body instance of the Collidable
     *
     * @param body
     */
    @Override
    public void setBody(Body body) {
        this.body = body;
        for(final Fixture fixture : this.body.getFixtureList()){
            fixture.setSensor(true);
        }
        this.body.setUserData(this);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose(){
        this.body.getWorld().destroyBody(this.body);
    }
}
