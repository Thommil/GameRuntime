package com.thommil.libgdx.runtime.test.basic.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.SpriteActor;
import com.thommil.libgdx.runtime.scene.actor.StaticActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class BasicActor extends StaticActor {

    private final static float xOffset = 1/6f;

    public BasicActor(final Texture texture, final float radius, int offset) {
        super(texture,MathUtils.random(-3f,3f),MathUtils.random(-3f,3f),radius,radius,xOffset*offset,1f,(1+offset)*xOffset,0f);
        this.setLayer(0);
    }
}
