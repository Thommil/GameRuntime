package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.actor.physics.SpriteBodyActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomtom on 17/02/16.
 */
public class DuckActor extends SpriteBodyActor {

    static Texture texture = new Texture(Gdx.files.internal("duck.png"));

    public DuckActor(final int layer, float x, float y) {
        super(texture);
        this.setSize(0.5f,0.5f);
        this.setOriginCenter();
        this.setPosition(x, y);
        this.setLayer(layer);
    }

    /**
     * Gets the Shapes of the Collidable
     */
    @Override
    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<Shape>();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.22f);
        shapes.add(circleShape);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.1f,0.1f,new Vector2(0,-0.1f),0);
        shapes.add(polygonShape);
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
        MassData massData = this.body.getMassData();
        massData.mass = 0.02f;
        this.body.setMassData(massData);
        //Override user data strategy as Duck is based on several fixtures
        this.body.getFixtureList().get(0).setUserData(this);
    }
}
