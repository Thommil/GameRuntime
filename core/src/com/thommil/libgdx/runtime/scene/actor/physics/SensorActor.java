package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.RigidBody;

import java.util.List;

/**
 * Simple sensor implementation
 *
 * @author thommil on 03/02/16.
 */
public abstract class SensorActor extends AbstractStepable implements RigidBody {

    /**
     * The bound body
     */
    public Body body;

    /**
     * Default constructor
     *
     * @param id The ID of the Actor in the scene
     */
    public SensorActor(final int id){
        super(id);
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
     * Sets the definition of Collidable, implementations
     * should configure Collidable settings in the passed
     * definition.
     *
     * @param bodyDef The collidable definition (settings)
     */
    @Override
    public void setDefinition(final BodyDef bodyDef) {
        bodyDef.type = BodyDef.BodyType.KinematicBody;
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
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    @Override
    public void step(long lastStepDuration) {
        //NOP
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose(){
        this.body.getWorld().destroyBody(this.body);
    }
}
