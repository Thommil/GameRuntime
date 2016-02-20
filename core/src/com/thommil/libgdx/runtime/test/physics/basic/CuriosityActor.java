package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;

import com.thommil.libgdx.runtime.scene.actor.physics.SpriteBodyActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomtom on 03/02/16.
 */
public class CuriosityActor extends SpriteBodyActor {

    public CuriosityActor(Texture texture) {
        super(MathUtils.random(0x7ffffffe), 0, texture);
        this.setSize(2.6f,2.3f);
        this.setOriginCenter();
        this.setPosition(0f,3f);
    }

    /**
     * Gets the Shapes of the Collidable
     */
    @Override
    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<Shape>();
        PolygonShape dynamicPolygonShape = new PolygonShape();
        dynamicPolygonShape.setAsBox(1f,1f);
        shapes.add(dynamicPolygonShape);
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
        body.getFixtureList().get(0).setRestitution(0.7f);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.texture.dispose();
    }
}
