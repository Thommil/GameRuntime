package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.actor.physics.SpriteBodyActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class DynamicPhysicsActor extends SpriteBodyActor {

    public DynamicPhysicsActor(Texture texture) {
        super(texture);
        this.setSize(2.6f,2.3f);
        this.setOriginCenter();
        this.setPosition(-100f,-100f);
        this.setLayer(1);
    }

    /**
     * Gets the Shape of the Collidable
     */
    @Override
    public Shape getShape() {
        PolygonShape dynamicPolygonShape = new PolygonShape();
        dynamicPolygonShape.setAsBox(1f,1f);
        return dynamicPolygonShape;
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef dynamicBodyDef = new BodyDef();
        dynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        dynamicBodyDef.position.set(MathUtils.random(-20f,20f),MathUtils.random(100,150f));
        dynamicBodyDef.angle = 0.1f;
        return dynamicBodyDef;
    }
}
