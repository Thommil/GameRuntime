package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.graphics.renderer.TextureSet;
import com.thommil.libgdx.runtime.scene.actor.physics.StaticBodyActor;

import java.util.ArrayList;
import java.util.List;

public class Tub extends StaticBodyActor {

    public Tub(final int layer, TextureSet textureSet, float x, float y, float width, float height, float u, float v, float u2, float v2, float color) {
        super(MathUtils.random(0x7ffffffe), layer, textureSet, x, y, width, height, u, v, u2, v2, color);
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

    @Override
    public void setDefinition(BodyDef bodyDef) {
        bodyDef.position.set(this.x + this.width/2, this.y + this.height/2);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.textureSet.dispose();
    }
}