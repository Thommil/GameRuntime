package com.thommil.libgdx.runtime.test.input.dynamic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.actor.PhysicsActor;

/**
 * Created by thommil on 03/02/16.
 */
public class StaticPhysicsActor extends PhysicsActor {

    public StaticPhysicsActor() {
        super(new Texture(Gdx.files.internal("planet.png")));
    }


    @Override
    public Body buildBody(World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(MathUtils.random(-10f,10f),MathUtils.random(-10f,10f));
        this.body = world.createBody(groundBodyDef);
        CircleShape planetShape = new CircleShape();
        final float radius = MathUtils.random(1f,3f);
        planetShape.setRadius(radius);
        this.body.createFixture(planetShape, 0f);
        planetShape.dispose();
        this.setLayer(0);
        this.setSize(radius*2,radius*2);
        this.setOriginCenter();
        this.setCenter(this.body.getPosition().x-radius,this.body.getPosition().y-radius);
        return this.body;
    }

    @Override
    public void step(long lastStepDuration) {

    }
}
