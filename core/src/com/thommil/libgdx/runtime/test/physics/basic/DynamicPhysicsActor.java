package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.physics.Physicable;
import com.thommil.libgdx.runtime.scene.Actor;

/**
 * Created by tomtom on 03/02/16.
 */
public class DynamicPhysicsActor extends Actor implements Renderable,Physicable {

    Sprite sprite;
    Texture texture;
    Body body;
    World world;

    public DynamicPhysicsActor() {
        this.texture = new Texture(Gdx.files.internal("curiosity.png"));
        this.sprite = new Sprite(texture);
        this.sprite.setSize(2.6f,2.3f);
        this.sprite.setOriginCenter();
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
        BodyDef dynamicBodyDef = new BodyDef();
        dynamicBodyDef.type = BodyDef.BodyType.DynamicBody;
        dynamicBodyDef.position.set(0f,3f);
        dynamicBodyDef.angle = 0.1f;
        this.body = this.world.createBody(dynamicBodyDef);
        PolygonShape dynamicPolygonShape = new PolygonShape();
        dynamicPolygonShape.setAsBox(1f,1f);
        this.body.createFixture(dynamicPolygonShape,1f).setRestitution(0.5f);
        dynamicPolygonShape.dispose();
    }

    @Override
    public int getBodyType() {
        return DYNAMIC;
    }

    @Override
    public Body getBody() {
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
        this.texture.dispose();
    }
}
