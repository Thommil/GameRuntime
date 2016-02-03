package com.thommil.libgdx.runtime.test.basic;

import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.graphics.Renderable;

/**
 * Created by tomtom on 03/02/16.
 */
public class SpriteActor extends Actor implements Renderable {

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    public void render(float deltaTime) {

    }
}
