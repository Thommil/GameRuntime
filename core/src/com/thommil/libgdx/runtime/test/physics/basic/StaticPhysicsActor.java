package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.actor.physics.StaticBodyActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class StaticPhysicsActor extends StaticBodyActor {

    public StaticPhysicsActor() {
        super(new Texture(Gdx.files.internal("test.png")),-2f,-2f,4f,1f,0f,1f,1f,0f, Color.WHITE.toFloatBits());
        this.texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
    }

    /**
     * Gets the Shape of the Collidable
     */
    @Override
    public Shape getShape() {
        PolygonShape groundBodyShape = new PolygonShape();
        groundBodyShape.setAsBox(2f,0.1f);
        return groundBodyShape;
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef groundBodyDef = super.getDefinition();
        groundBodyDef.position.set(0, -1.9f);
        return groundBodyDef;
    }

}
