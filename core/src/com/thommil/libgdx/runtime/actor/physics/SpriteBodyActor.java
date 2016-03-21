package com.thommil.libgdx.runtime.actor.physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;

/**
 * Defines a dynamic SpriteActor actor which can interact with physic world
 *
 * @author thommil on 03/02/16.
 */
public abstract class SpriteBodyActor extends SpriteActor implements RigidBody {

    public Body body;

    public SpriteBodyActor(final int id, TextureSet textureSet) {
        super(id, textureSet);
    }

    public SpriteBodyActor(final int id, TextureSet textureSet, int srcWidth, int srcHeight) {
        super(id, textureSet, srcWidth, srcHeight);
    }

    public SpriteBodyActor(final int id, TextureSet textureSet, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(id, textureSet, srcX, srcY, srcWidth, srcHeight);
    }

     /**
     * Gets the definition of Collidable.
     *
     * @return definition The collidable definition (settings)
     */
    @Override
    public BodyDef getDefinition() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(this.x, this.y);
        bodyDef.angle = this.rotation / MathUtils.radDeg;
        return bodyDef;
    }

    /**
     * Gets the fixtures definition of the Collidable
     */
    @Override
    public abstract Array<FixtureDef> getFixturesDefinition();

    /**
     * Set body instance of the Collidable
     *
     * @param body The Body instance
     */
    @Override
    public void setBody(final Body body) {
        this.body = body;
        this.body.setUserData(this);
    }

    /**
     * Get the collidable body
     */
    @Override
    public Body getBody() {
        return this.body;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        if(this.body != null) {
            if (!this.dirty) {
                final Vector2 position = this.body.getPosition();
                final float angle = this.body.getAngle();
                final float xAmount = position.x - this.width / 2 - this.x;
                final float yAmount = position.y - this.height / 2 - this.y;
                this.x += xAmount;
                this.y += yAmount;
                this.rotation = angle * MathUtils.radDeg;

                if (this.dirty) return;

                this.vertices[X1] += xAmount;
                this.vertices[X2] += xAmount;
                this.vertices[X3] += xAmount;
                this.vertices[X4] += xAmount;
                this.vertices[Y1] += yAmount;
                this.vertices[Y2] += yAmount;
                this.vertices[Y3] += yAmount;
                this.vertices[Y4] += yAmount;

                this.dirty = true;
            }

            super.render(deltaTime, renderer);
        }
    }
}
