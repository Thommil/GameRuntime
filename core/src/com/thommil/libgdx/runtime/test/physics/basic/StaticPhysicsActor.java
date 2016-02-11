package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.actor.PhysicsActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class StaticPhysicsActor extends PhysicsActor{

    public StaticPhysicsActor() {
        super(new Texture(Gdx.files.internal("ground.jpg")));
    }


    @Override
    public Body buildBody(World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0, -1.9f);
        this.body = world.createBody(groundBodyDef);
        PolygonShape groundBodyShape = new PolygonShape();
        groundBodyShape.setAsBox(2f,0.1f);
        this.body.createFixture(groundBodyShape, 0f);
        groundBodyShape.dispose();
        return this.body;
    }

    @Override
    public void step(long lastStepDuration) {

    }
}
