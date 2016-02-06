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
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.PhysicsActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class StaticPhysicsActor extends PhysicsActor implements Renderable {

    Texture texture;

    public StaticPhysicsActor() {
        this.texture = new Texture(Gdx.files.internal("ground.jpg"));
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);

    }


    @Override
    protected Body buildBody(World world) {
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
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime, Batch batch) {
        batch.draw(texture,-2f,-2f,4f,1.5f);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
