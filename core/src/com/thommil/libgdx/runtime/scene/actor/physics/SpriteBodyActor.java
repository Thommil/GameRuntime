package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.graphics.batch.SpriteBatch;
import com.thommil.libgdx.runtime.scene.RigidBody;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;

import java.util.List;

/**
 * Defines a dynamic SpriteActor actor which can interact with physic world
 *
 * Created by thommil on 03/02/16.
 */
public abstract class SpriteBodyActor extends SpriteActor implements RigidBody {

    public Body body;

    public SpriteBodyActor(final int id, final int layer, Texture texture) {
        super(id, layer, texture);
    }

    public SpriteBodyActor(final int id, final int layer, Texture texture, int srcWidth, int srcHeight) {
        super(id, layer, texture, srcWidth, srcHeight);
    }

    public SpriteBodyActor(final int id, final int layer, Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(id, layer, texture, srcX, srcY, srcWidth, srcHeight);
    }

    public SpriteBodyActor(final int id, final int layer, TextureRegion region) {
        super(id, layer, region);
    }

    public SpriteBodyActor(final int id, final int layer, TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(id, layer, region, srcX, srcY, srcWidth, srcHeight);
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
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(this.x, this.y);
        bodyDef.angle = this.rotation / DEG_IN_RAD;
        return bodyDef;
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
    public void render(float deltaTime, SpriteBatch renderer) {
        if(!dirty) {
            final Vector2 position = this.body.getPosition();
            final float angle = this.body.getAngle();
            final float xAmount = position.x - width / 2 - this.x;
            final float yAmount = position.y - height / 2 - this.y;
            this.x += xAmount;
            this.y += yAmount;
            this.rotation = angle * DEG_IN_RAD;

            if(dirty) return;

            vertices[X1] += xAmount;
            vertices[X2] += xAmount;
            vertices[X3] += xAmount;
            vertices[X4] += xAmount;
            vertices[Y1] += yAmount;
            vertices[Y2] += yAmount;
            vertices[Y3] += yAmount;
            vertices[Y4] += yAmount;

            dirty = true;
        }

        super.render(deltaTime, renderer);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.body.getWorld().destroyBody(this.body);
    }
}
