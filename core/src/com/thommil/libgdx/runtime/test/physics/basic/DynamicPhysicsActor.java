package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.thommil.libgdx.runtime.scene.actor.PhysicsActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class DynamicPhysicsActor extends PhysicsActor{

    public DynamicPhysicsActor(Texture texture) {
        super(texture);
        this.setSize(2.6f,2.3f);
        this.setOriginCenter();
    }

    @Override
    public Body buildBody(World world) {
        BodyDef dynamicBodyDef = new BodyDef();
        dynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        dynamicBodyDef.position.set(0f,3f);
        dynamicBodyDef.angle = 0.1f;
        this.body = world.createBody(dynamicBodyDef);
        PolygonShape dynamicPolygonShape = new PolygonShape();
        dynamicPolygonShape.setAsBox(1f,1f);
        this.body.createFixture(dynamicPolygonShape,1f).setRestitution(0.5f);
        dynamicPolygonShape.dispose();
        return this.body;
    }

}
