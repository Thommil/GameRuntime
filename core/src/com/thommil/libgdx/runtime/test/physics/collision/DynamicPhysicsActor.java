package com.thommil.libgdx.runtime.test.physics.collision;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.actor.PhysicsSpriteActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class DynamicPhysicsActor extends PhysicsSpriteActor {

    public DynamicPhysicsActor(Texture texture) {
        super(texture);
        this.setSize(2.6f,2.3f);
        this.setOriginCenter();
        this.setLayer(1);
    }

    @Override
    public void buildBody(World world) {
        BodyDef dynamicBodyDef = new BodyDef();
        dynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        dynamicBodyDef.position.set(MathUtils.random(-20f,20f),100f);
        dynamicBodyDef.angle = 0.1f;
        this.body = world.createBody(dynamicBodyDef);
        PolygonShape dynamicPolygonShape = new PolygonShape();
        dynamicPolygonShape.setAsBox(1f,1f);
        Fixture fixture = this.body.createFixture(dynamicPolygonShape,1f);
        fixture.setRestitution(0.0f);
        fixture.setFriction(1f);
        dynamicPolygonShape.dispose();
    }
}
