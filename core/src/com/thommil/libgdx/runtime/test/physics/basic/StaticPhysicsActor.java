package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.physics.Physicable;
import com.thommil.libgdx.runtime.scene.Actor;

/**
 * Created by tomtom on 03/02/16.
 */
public class StaticPhysicsActor extends Actor implements Renderable,Physicable {

    Texture texture;
    Body body;
    World world;

    public StaticPhysicsActor() {
        this.texture = new Texture(Gdx.files.internal("ground.jpg"));
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0, -1.9f);
        this.body = this.world.createBody(groundBodyDef);
        PolygonShape groundBodyShape = new PolygonShape();
        groundBodyShape.setAsBox(10f,0.1f);
        this.body.createFixture(groundBodyShape, 0f);
        groundBodyShape.dispose();
    }

    @Override
    public int getBodyType() {
        return STATIC;
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
        batch.draw(texture,-10,-2,20,0.2f);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
