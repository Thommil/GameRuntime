package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.scene.RigidBody;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;

import java.util.List;

/**
 * Created by tomtom on 12/02/16.
 */
public abstract class StaticBodyActor extends StaticActor implements RigidBody {

    /**
     * The bound body
     */
    public Body body;

    public StaticBodyActor(final int id, final int layer, Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2, final float color) {
        super(id, layer, texture, x, y, width, height, u, v, u2, v2, color);
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
        return 1.0f;
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(this.x, this.y);
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
        this.body.setUserData(this);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        super.dispose();
        this.body.getWorld().destroyBody(this.body);
    }

}
