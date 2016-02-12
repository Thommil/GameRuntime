package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.actor.PhysicsStaticActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class StaticPhysicsActor extends PhysicsStaticActor {

    public StaticPhysicsActor() {
        super(new Texture(Gdx.files.internal("ground.jpg")),-1000f,-50f,2000f,10f,0f,0.05f,10f,0f);
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        this.setLayer(0);
    }


    @Override
    public void buildBody(World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0, -45f);
        this.body = world.createBody(groundBodyDef);
        PolygonShape groundBodyShape = new PolygonShape();
        groundBodyShape.setAsBox(1000f,5f);
        this.body.createFixture(groundBodyShape, 0f);
        groundBodyShape.dispose();
    }
}