package com.thommil.libgdx.runtime.test.physics.collision;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
public class GroundActor extends StaticBodyActor {

    public GroundActor(final Texture texture, final float x, final float y, final float width, final float height) {
        super(texture,x,y,width,height,0f,1f,1f,0f, Color.WHITE.toFloatBits());
        this.setLayer(0);
    }

    /**
     * Gets the Shapes of the Collidable
     */
    @Override
    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<Shape>();
        PolygonShape groundBodyShape = new PolygonShape();
        groundBodyShape.setAsBox(this.width/2,this.height/2, new Vector2(this.width/2,this.height/2),0);
        shapes.add(groundBodyShape);
        return shapes;
    }

}