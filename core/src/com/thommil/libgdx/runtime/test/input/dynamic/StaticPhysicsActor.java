package com.thommil.libgdx.runtime.test.input.dynamic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.actor.physics.SpriteBodyActor;

/**
 * Created by thommil on 03/02/16.
 */
public class StaticPhysicsActor extends SpriteBodyActor {

    static Texture texture = new Texture(Gdx.files.internal("planet.png"));

    float radius;

    public StaticPhysicsActor() {
        super(texture);
        this.setOriginCenter();
        this.setLayer(0);
        //this.setPosition(this.body.getPosition().x-radius,this.body.getPosition().y-radius);
    }

    /**
     * Gets the Shape of the Collidable
     */
    @Override
    public Shape getShape() {
        CircleShape planetShape = new CircleShape();
        radius = MathUtils.random(1f,3f);
        planetShape.setRadius(radius);
        return planetShape;
    }

    /**
     * Gets the definition of Collidable
     */
    @Override
    public BodyDef getDefinition() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(MathUtils.random(-10f,10f),MathUtils.random(-10f,10f));
        return groundBodyDef;
    }

    /**
     * Set body instance of the Collidable
     *
     * @param body
     */
    @Override
    public void setBody(Body body) {
        super.setBody(body);
        this.setPosition(this.body.getPosition().x-radius,this.body.getPosition().y-radius);
    }
}
