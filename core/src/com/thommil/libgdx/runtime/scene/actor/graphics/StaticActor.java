package com.thommil.libgdx.runtime.scene.actor.graphics;

import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;

/**
 * Basic StaticActor using custom for rendering using low level API
 *
 * @author thommil on 03/02/16.
 */
public class StaticActor extends Actor implements Renderable<SpriteBatchRenderer> {

    protected int layer = 0;

    final public TextureSet textureSet;
    final float[] vertices = new float[SpriteActor.SPRITE_SIZE];
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
     * @param textureSet The TextureSet to use
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
                        final TextureSet textureSet,
                        final float x, final float y,
                        final float width, final float height,
                        final float u, final float v,
                        final float u2, final float v2,
                        final float color){
        super(id);
        this.layer = layer;
        this.textureSet = textureSet;

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
        this.color = color;

        vertices[SpriteActor.X1] = x;
        vertices[SpriteActor.Y1] = y;
        vertices[SpriteActor.C1] = color;
        vertices[SpriteActor.U1] = u;
        vertices[SpriteActor.V1] = v;

        vertices[SpriteActor.X2] = x;
        vertices[SpriteActor.Y2] = y + height;
        vertices[SpriteActor.C2] = color;
        vertices[SpriteActor.U2] = u;
        vertices[SpriteActor.V2] = v2;

        vertices[SpriteActor.X3] = x + width;
        vertices[SpriteActor.Y3] = y + height;
        vertices[SpriteActor.C3] = color;
        vertices[SpriteActor.U3] = u2;
        vertices[SpriteActor.V3] = v2;

        vertices[SpriteActor.X4] = x + width;
        vertices[SpriteActor.Y4] = y;
        vertices[SpriteActor.C4] = color;
        vertices[SpriteActor.U4] = u2;
        vertices[SpriteActor.V4] = v;
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
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        renderer.setTextureSet(this.textureSet);
        renderer.draw(this.vertices);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        //NOP Texture can be shared
    }
}
