package com.thommil.libgdx.runtime.test.physics.collision;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;

/**
 * Created by tomtom on 13/02/16.
 */
public class ExplosionActor extends SpriteActor {

    public ExplosionActor(Texture texture) {
        super(MathUtils.random(0x7ffffffe), 1, texture);
        this.setSize(3f,3f);
        this.setOriginCenter();
    }
}
