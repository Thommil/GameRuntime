package com.thommil.libgdx.runtime.test.physics.collision;

import com.badlogic.gdx.graphics.Texture;
import com.thommil.libgdx.runtime.scene.actor.SpriteActor;

/**
 * Created by tomtom on 13/02/16.
 */
public class ExplosionActor extends SpriteActor {

    public ExplosionActor(Texture texture) {
        super(texture);
        this.setSize(3f,3f);
        this.setOriginCenter();
        this.setLayer(1);
    }
}
