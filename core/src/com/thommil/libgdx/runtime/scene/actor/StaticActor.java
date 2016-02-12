package com.thommil.libgdx.runtime.scene.actor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Basic StaticActor using custom for BasicBatch rendering using low level API
 *
 * Created by thommil on 12/02/16.
 */
public class StaticActor implements Actor, Renderable<Batch> {

    protected int layer = 0;

    final public Texture texture;
    final public float x, y;
    final public float width, height;
    final public float u, v;
    final public float u2, v2;

    public StaticActor(final Texture texture,
                       final float x, final float y,
                       final float width, final float height,
                       final float u, final float v,
                       final float u2, final float v2){
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;

    }

    public void setLayer(final int layer){
        this.layer = layer;
    }

    @Override
    public int getLayer(){
        return this.layer;
    }

    @Override
    public void render(float deltaTime, Batch renderer) {
        renderer.draw(texture, x, y, width, height, u, v, u2, v2);
    }

    @Override
    public void dispose() {
        //NOP Texture can be shared
    }
}
