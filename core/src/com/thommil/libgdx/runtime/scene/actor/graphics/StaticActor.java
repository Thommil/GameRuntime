package com.thommil.libgdx.runtime.scene.actor.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.graphics.batch.SpriteBatch;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Basic StaticActor using custom for BasicBatch rendering using low level API
 *
 * Created by thommil on 12/02/16.
 */
public class StaticActor extends Actor implements Renderable<SpriteBatch> {

    protected int layer = 0;

    final public Texture texture;
    final public float x, y;
    final public float width, height;
    final public float u, v;
    final public float u2, v2;
    final public float color;

    /**
     * Default conctructor
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer ID of the renderable in the scene
     * @param texture The texture to use
     * @param x The X coord
     * @param y The Y coord
     * @param width The width of the renderable
     * @param height The height of the renderable
     * @param u
     * @param v
     * @param u2
     * @param v2
     * @param color
     */
    public StaticActor(final int id,
                        final int layer,
                        final Texture texture,
                        final float x, final float y,
                        final float width, final float height,
                        final float u, final float v,
                        final float u2, final float v2,
                        final float color){
        super(id);
        this.layer = layer;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
        this.color = color;
    }

    public void setLayer(final int layer){
        this.layer = layer;
    }

    /**
     * Returns the layer of the Renderable
     *
     * @return The layer of the Renderable
     */
    @Override
    public int getLayer() {
        return this.layer;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatch renderer) {
        renderer.draw(texture, x, y, width, height, u, v, u2, v2, color);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        //NOP Texture can be shared
    }
}
