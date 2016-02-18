package com.thommil.libgdx.runtime.test.basic.cache;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;

/**
 * Created by tomtom on 03/02/16.
 */
public class PlanetActor extends StaticActor {

    private final static float xOffset = 1/6f;

    public PlanetActor(final Texture texture, final float radius, int offset) {
        super(MathUtils.random(0x7ffffffe), 0, texture,MathUtils.random(-4.5f,3.5f),MathUtils.random(-4.5f,3.5f),radius,radius,xOffset*offset,1f,(1+offset)*xOffset,0f, new Color(1,1,1,0.8f).toFloatBits());
    }

    @Override
    public void dispose() {
        super.dispose();
        this.texture.dispose();
    }
}
