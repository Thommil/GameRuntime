package com.thommil.libgdx.runtime.test.physics.collision;

import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.scene.actor.graphics.SpriteActor;

/**
 * Created by tomtom on 13/02/16.
 */
public class ExplosionActor extends SpriteActor {

    public ExplosionActor(TextureSet textureSet) {
        super(MathUtils.random(0x7ffffffe), 0, textureSet);
        this.setSize(3f,3f);
        this.setOriginCenter();
    }
}
