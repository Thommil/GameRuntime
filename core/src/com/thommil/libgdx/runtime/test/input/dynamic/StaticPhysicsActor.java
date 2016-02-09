package com.thommil.libgdx.runtime.test.input.dynamic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.scene.PhysicsActor;

/**
 * Created by thommil on 03/02/16.
 */
public class StaticPhysicsActor extends PhysicsActor implements Renderable<SpriteBatch> {

    Texture texture;
    float[] textureBounds = new float[4];
    float radius;

    public StaticPhysicsActor() {
        this.texture = new Texture(Gdx.files.internal("planet.png"));
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
    }


    @Override
    protected Body buildBody(World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(MathUtils.random(-10f,10f),MathUtils.random(-10f,10f));
        this.body = world.createBody(groundBodyDef);
        CircleShape planetShape = new CircleShape();
        radius = MathUtils.random(1f,3f);
        planetShape.setRadius(radius);
        this.body.createFixture(planetShape, 0f);
        planetShape.dispose();
        textureBounds[0] = this.body.getPosition().x-radius;
        textureBounds[1] = this.body.getPosition().y-radius;
        textureBounds[2] = radius*2;
        textureBounds[3] = radius*2;
        return this.body;
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime, SpriteBatch batch) {
        batch.draw(texture,textureBounds[0],textureBounds[1],textureBounds[2],textureBounds[3]);
    }

    @Override
    protected void step(long lastStepDuration) {

    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
