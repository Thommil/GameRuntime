package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.scene.PhysicsActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class DynamicPhysicsActor extends PhysicsActor implements Renderable{

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
        dynamicBodyDef.position.set(MathUtils.random(-20f,20f),MathUtils.random(50f,60f));
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
    public void render(float deltaTime, Batch batch) {
        this.sprite.setCenter(this.renderComponents[X],this.renderComponents[Y]);
        this.sprite.setRotation(this.renderComponents[ANGLE]*57.2957795f);
        this.sprite.draw(batch);
    }

    @Override
    public void dispose() {

    }
}
