package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.scene.actor.physics.StaticBodyActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomtom on 03/02/16.
 */
public class Tub extends StaticBodyActor {

    public Tub(final int layer, Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2, float color) {
        super(MathUtils.random(0x7ffffffe), layer, texture, x, y, width, height, u, v, u2, v2, color);
    }

    /**
     * Gets the Shape of the Collidable
     */
    @Override
    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<Shape>();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(this.width/2 - 0.05f,this.height/2, new Vector2(0,0),0f);
        shapes.add(shape);
        return shapes;
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef groundBodyDef = super.getDefinition();
        groundBodyDef.position.set(this.x + this.width/2, this.y + this.height/2);
        return groundBodyDef;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.texture.dispose();
    }
}