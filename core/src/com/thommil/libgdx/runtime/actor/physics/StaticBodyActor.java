package com.thommil.libgdx.runtime.actor.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.actor.graphics.StaticActor;

/**
 * Defines a static renderable actor which can interact with physic world
 *
 * @author thommil on 03/02/16.
 */
public abstract class StaticBodyActor extends StaticActor implements RigidBody {

    /**
     * The bound body
     */
    public Body body;

    public StaticBodyActor(final int id, TextureSet textureSet, float x, float y, float width, float height, float u, float v, float u2, float v2, final float color) {
        super(id, textureSet, x, y, width, height, u, v, u2, v2, color);
    }

    public StaticBodyActor(int id, TextureSet textureSet, float x, float y, float width, float height, int regionX, int regionY, int regionWidth, int regionHeight, float color) {
        super(id, textureSet, x, y, width, height, regionX, regionY, regionWidth, regionHeight, color);
    }

    /**
     * Gets the definition of Collidable.
     *
     * @return definition The collidable definition (settings)
     */
    @Override
    public BodyDef getDefinition() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(this.x, this.y);
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
     * @param body The body instance
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
}
