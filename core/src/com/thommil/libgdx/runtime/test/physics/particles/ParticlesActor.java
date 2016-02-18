package com.thommil.libgdx.runtime.test.physics.particles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.thommil.libgdx.runtime.scene.actor.physics.SoftBodyActor;
import com.thommil.libgdx.runtime.scene.actor.physics.SpriteBodyActor;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

/**
 * Created by tomtom on 03/02/16.
 */
public class ParticlesActor extends SoftBodyActor {

    public ParticlesActor() {
        super(MathUtils.random(0x7ffffffe), 0);
    }

    /**
     * Gets the definition of SoftBody
     *
     * @return The definition of the soft body in a particle system
     */
    @Override
    public ParticleSystemDef getDefinition() {
        ParticleSystemDef particleSystemDef = new ParticleSystemDef();
        particleSystemDef.radius = 0.025f;
        return particleSystemDef;
    }
}
