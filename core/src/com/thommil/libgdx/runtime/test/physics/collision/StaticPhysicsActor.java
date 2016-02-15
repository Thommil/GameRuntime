package com.thommil.libgdx.runtime.test.physics.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.actor.physics.StaticBodyActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class StaticPhysicsActor extends StaticBodyActor {

    public StaticPhysicsActor() {
        super(new Texture(Gdx.files.internal("ground.jpg")),-1000f,-50f,2000f,10f,0f,0.05f,10f,0f, Color.WHITE.toFloatBits());
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        this.setLayer(0);
    }

    /**
     * Gets the Shape of the Collidable
     */
    @Override
    public Shape getShape() {
        PolygonShape groundBodyShape = new PolygonShape();
        groundBodyShape.setAsBox(1000f,5f);
        return groundBodyShape;
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef groundBodyDef = super.getDefinition();
        groundBodyDef.position.set(0, -45f);
        return groundBodyDef;
    }
}