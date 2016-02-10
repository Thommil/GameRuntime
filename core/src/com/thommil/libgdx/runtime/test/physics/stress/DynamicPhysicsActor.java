package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.Renderable;
import com.thommil.libgdx.runtime.scene.actor.PhysicsActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class DynamicPhysicsActor extends PhysicsActor implements Renderable<SpriteBatch>{

    Sprite sprite;

    public DynamicPhysicsActor(Texture texture) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(2.6f,2.3f);
        this.sprite.setOriginCenter();
    }

    @Override
    protected Body buildBody(World world) {
        BodyDef dynamicBodyDef = new BodyDef();
        dynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        dynamicBodyDef.position.set(MathUtils.random(-20f,20f),MathUtils.random(100,150f));
        dynamicBodyDef.angle = 0.1f;
        this.body = world.createBody(dynamicBodyDef);
        PolygonShape dynamicPolygonShape = new PolygonShape();
        dynamicPolygonShape.setAsBox(1f,1f);
        Fixture fixture = this.body.createFixture(dynamicPolygonShape,1f);
        fixture.setRestitution(0.0f);
        fixture.setFriction(1f);

        dynamicPolygonShape.dispose();
        return this.body;
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        this.sprite.draw(batch);
    }

    @Override
    protected void step(long lastStepDuration) {
        this.sprite.setCenter(this.body.getPosition().x,this.body.getPosition().y);
        this.sprite.setRotation(this.body.getAngle()*57.2957795f);
    }

    @Override
    public void dispose() {

    }
}
