package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.scene.actor.physics.SpriteBodyActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomtom on 03/02/16.
 */
public class CuriosityActor extends SpriteBodyActor {

    public CuriosityActor(int id, TextureSet textureSet) {
        super(id, 0, textureSet);
        this.setSize(2.6f,2.3f);
        this.setOriginCenter();
        this.setPosition(MathUtils.random(-20f,20f),MathUtils.random(100,150f));
        this.setRotation(MathUtils.random(0f,90f));
    }

    public CuriosityActor(TextureSet textureSet) {
        super(MathUtils.random(0x7ffffffe), 0, textureSet);
        this.setSize(2.6f,2.3f);
        this.setOriginCenter();
        this.setPosition(MathUtils.random(-20f,20f),MathUtils.random(100,150f));
        this.setRotation(MathUtils.random(0f,90f));
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


}
