package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.RigidBody;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;

/**
 * Created by tomtom on 12/02/16.
 */
public abstract class StaticBodyActor extends StaticActor implements RigidBody {

    /**
     * The bound body
     */
    public Body body;

    public StaticBodyActor(final int id, Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2, final float color) {
        super(id, texture, x, y, width, height, u, v, u2, v2, color);
    }

    public StaticBodyActor(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2, final float color) {
        super(texture, x, y, width, height, u, v, u2, v2, color);
    }

    /**
     * Gets the Shape of the Collidable
     */
    @Override
    public abstract Shape getShape();

    /**
     * Gets the density to the RigidBody
     */
    @Override
    public float getDensity() {
        return 1.0f;
    }

    /**
     * Gets the friction to the RigidBody
     */
    @Override
    public float getFriction() {
        return 0.2f;
    }

    /**
     * Gets the restitution to the RigidBody
     */
    @Override
    public float getRestitution() {
        return 0.0f;
    }

    /**
     * Sets the density to the RigidBody
     */
    public void setDensity(final float density){
        this.body.getFixtureList().get(0).setDensity(density);
    }

    /**
     * Sets the friction to the RigidBody
     */
    public void setFriction(final float friction){
        this.body.getFixtureList().get(0).setFriction(friction);
    }

    /**
     * Sets the restitution to the RigidBody
     */
    public void setRestitution(final float restitution){
        this.body.getFixtureList().get(0).setRestitution(restitution);
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        return groundBodyDef;
    }

    /**
     * Set body instance of the Collidable
     *
     * @param body
     */
    @Override
    public void setBody(Body body) {
        this.body = body;
        this.body.setUserData(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.body.getWorld().destroyBody(this.body);
    }
}
