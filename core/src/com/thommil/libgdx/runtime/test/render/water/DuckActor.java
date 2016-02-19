package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.actor.physics.SpriteBodyActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomtom on 17/02/16.
 */
public class DuckActor extends SpriteBodyActor {


    public DuckActor(final Texture texture, final int layer, float x, float y) {
        super(MathUtils.random(0x7ffffffe), layer, texture);
        this.setSize(0.5f,0.5f);
        this.setOriginCenter();
        this.setPosition(x, y);
    }

    /**
     * Gets the Shapes of the Collidable
     */
    @Override
    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<Shape>();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.25f);
        shapes.add(circleShape);
        return shapes;
    }

    /**
     * Set body instance of the Collidable
     *
     * @param body
     */
    @Override
    public void setBody(Body body) {
        super.setBody(body);
        this.setDensity(0.5f);
        MassData massData = body.getMassData();
        massData.center.set(0,-0.1f);
        body.setMassData(massData);
    }
}