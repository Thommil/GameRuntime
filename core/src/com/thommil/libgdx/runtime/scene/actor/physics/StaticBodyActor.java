package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.graphics.renderer.TextureSet;
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

    protected float density = 1f;

    public StaticBodyActor(final int id, final int layer, TextureSet textureSet, float x, float y, float width, float height, float u, float v, float u2, float v2, final float color) {
        super(id, layer, textureSet, x, y, width, height, u, v, u2, v2, color);
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
        return this.density;
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
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(this.x, this.y);
    }

    /**
     * Set body instance of the Collidable
     *
     * @param body
     */
    @Override
    public void setBody(final Body body) {
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
