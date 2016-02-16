package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.scene.actor.physics.StaticBodyActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class GroundActor extends StaticBodyActor {

    public GroundActor(final Texture texture, final float x, final float y, final float width, final float height) {
        super(texture,x,y,width,height,0f,1f,1f,0f, Color.WHITE.toFloatBits());
        this.setLayer(0);
    }

    /**
     * Gets the Shape of the Collidable
     */
    @Override
    public Shape getShape() {
        PolygonShape groundBodyShape = new PolygonShape();
        groundBodyShape.setAsBox(this.width/2,this.height/2);
        return groundBodyShape;
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef groundBodyDef = super.getDefinition();
        groundBodyDef.position.set(this.x + this.width/2, this.y - this.height/2);
        return groundBodyDef;
    }

}