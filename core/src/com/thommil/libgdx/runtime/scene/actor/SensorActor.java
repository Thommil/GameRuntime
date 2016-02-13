package com.thommil.libgdx.runtime.scene.actor;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.Collidable;

/**
 * Simple sensor implementation
 *
 * Created by thommil on 13/02/16.
 */
public class SensorActor implements Collidable {

    protected final int id;

    /**
     * The bound body
     */
    public Body body;

    /**
     * The Shape of the sensor
     */
    protected final Shape shape;

    public SensorActor(final Shape shape){
        this(MathUtils.random(0x7ffffffe), shape);
    }

    public SensorActor(final int id, final Shape shape){
        this.id = id;
        this.shape = shape;
    }

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Subclasses must implement this method to build the body
     * of this instance.
     *
     * @param world
     */
    @Override
    public void buildBody(World world){
        final BodyDef dynamicBodyDef = new BodyDef();
        dynamicBodyDef.type = BodyDef.BodyType.KinematicBody;
        this.body = world.createBody(dynamicBodyDef);
        this.body.createFixture(shape,1f).setSensor(true);
        shape.dispose();
    }

    /**
     * @return The body of this instance
     */
    @Override
    public Body getBody(){
        return this.body;
    }

    /**
     * Called at each physics step, any physics related task should be
     * handled here and not in the rendering phase.
     *
     * @param lastStepDuration The duration of the last step for QoS purpose
     */
    @Override
    public void step(long lastStepDuration){
        //NOP
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose(){
        //NOP
    }
}
