package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
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
        groundBodyDef.position.set(0, -45f);
        this.body = world.createBody(groundBodyDef);
        PolygonShape groundBodyShape = new PolygonShape();
        groundBodyShape.setAsBox(1000f,5f);
        this.body.createFixture(groundBodyShape, 0f);
        groundBodyShape.dispose();

        return this.body;
    }
}
