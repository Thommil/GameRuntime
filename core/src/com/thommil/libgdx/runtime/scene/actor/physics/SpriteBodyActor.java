package com.thommil.libgdx.runtime.scene.actor.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.graphics.batch.SpriteBatch;
import com.thommil.libgdx.runtime.scene.RigidBody;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;

/**
 * Defines a dynamic SpriteActor actor which can interact with physic world
 *
 * Created by thommil on 03/02/16.
 */
public abstract class SpriteBodyActor extends SpriteActor implements RigidBody {

    public Body body;

    public SpriteBodyActor(Texture texture) {
        super(texture);
    }

    public SpriteBodyActor(final int id, Texture texture) {
        super(id, texture);
    }

    public SpriteBodyActor(Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
    }

    public SpriteBodyActor(final int id, Texture texture, int srcWidth, int srcHeight) {
        super(id, texture, srcWidth, srcHeight);
    }

    public SpriteBodyActor(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
    }

    public SpriteBodyActor(final int id, Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(id, texture, srcX, srcY, srcWidth, srcHeight);
    }

    public SpriteBodyActor(TextureRegion region) {
        super(region);
    }

    public SpriteBodyActor(final int id, TextureRegion region) {
        super(id, region);
    }

    public SpriteBodyActor(TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(region, srcX, srcY, srcWidth, srcHeight);
    }

    public SpriteBodyActor(final int id, TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(id, region, srcX, srcY, srcWidth, srcHeight);
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
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.DynamicBody;
        return groundBodyDef;
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
        final Vector2 position = this.body.getPosition();
        final float angle = this.body.getAngle();
        final float xAmount = position.x - width / 2 - this.x;
        final float yAmount = position.y - height / 2 - this.y;
        this.x += xAmount;
        this.y += yAmount;
        this.rotation = angle * DEG_IN_RAD;
        dirty = true;

        vertices[X1] += xAmount;
        vertices[X2] += xAmount;
        vertices[X3] += xAmount;
        vertices[X4] += xAmount;
        vertices[Y1] += yAmount;
        vertices[Y2] += yAmount;
        vertices[Y3] += yAmount;
        vertices[Y4] += yAmount;

        super.render(deltaTime, renderer);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.body.getWorld().destroyBody(this.body);
    }
}