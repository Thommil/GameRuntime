package com.thommil.libgdx.runtime.test.input.dynamic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.graphics.renderer.TextureSet;
import com.thommil.libgdx.runtime.scene.actor.physics.SpriteBodyActor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thommil on 03/02/16.
 */
public class PlanetActor extends SpriteBodyActor {

    static TextureSet textureSet = new TextureSet(new Texture(Gdx.files.internal("planet.png")));
    float radius;

    public PlanetActor() {
        super(MathUtils.random(0x7ffffffe), 0, textureSet);
        this.setOriginCenter();
        this.setPosition(MathUtils.random(-10f,10f),MathUtils.random(-10f,10f));
    }

    /**
     * Gets the Shapes of the Collidable
     */
    @Override
    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<Shape>();
        CircleShape planetShape = new CircleShape();
        radius = MathUtils.random(1f,3f);
        planetShape.setRadius(radius);
        this.setSize(radius*2,radius*2);
        shapes.add(planetShape);
        return shapes;
    }

    @Override
    public void setDefinition(BodyDef bodyDef) {
        super.setDefinition(bodyDef);
        bodyDef.type = BodyDef.BodyType.StaticBody;
    }

    @Override
    public void dispose() {
        super.dispose();
        PlanetActor.textureSet.dispose();
    }
}
