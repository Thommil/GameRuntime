package com.thommil.libgdx.runtime.actor.graphics;

import com.badlogic.gdx.math.Rectangle;
import com.thommil.libgdx.runtime.events.TouchListener;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.actor.Actor;

/**
 * Basic StaticActor using custom for rendering using low level API. Can be rendered
 * using a SpriteBatchRenderer but intended to be added in a SpriteCacheRenderer.
 *
 * @author thommil on 03/02/16.
 */
public class StaticActor extends Actor implements Renderable<SpriteBatchRenderer>, TouchListener {

    final public TextureSet textureSet;
    final float[] vertices = new float[SpriteActor.SPRITE_SIZE];
    final public float x, y;
    final public float width, height;
    final public float u, v;
    final public float u2, v2;
    final public float color;

    protected Rectangle bound;

    /**
     * Default conctructor with UV
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer ID of the renderable in the scene
     * @param textureSet The TextureSet to use
     * @param x The X coord
     * @param y The Y coord
     * @param width The width of the renderable
     * @param height The height of the renderable
     * @param u u Texture coordinates
     * @param v v Texture coordinates
     * @param u2 u2 Texture coordinates
     * @param v2 v2 Texture coordinates
     * @param color The color modifier
     */
    public StaticActor(final int id,
                        final TextureSet textureSet,
                        final float x, final float y,
                        final float width, final float height,
                        float u, float v,
                        float u2, float v2,
                        final float color){
        super(id);
        this.textureSet = textureSet;

        final int texWidth = this.textureSet.getWidth(), texHeight = this.textureSet.getHeight();
        final int regionWidth = Math.round(Math.abs(u2 - u) * texWidth);
        final int regionHeight = Math.round(Math.abs(v2 - v) * texHeight);

        if (regionWidth == 1 && regionHeight == 1) {
            final float adjustX = 0.25f / texWidth;
            u += adjustX;
            u2 -= adjustX;
            final float adjustY = 0.25f / texHeight;
            v += adjustY;
            v2 -= adjustY;
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;
        this.color = color;

        this.vertices[SpriteActor.X1] = x;
        this.vertices[SpriteActor.Y1] = y;
        this.vertices[SpriteActor.C1] = color;
        this.vertices[SpriteActor.U1] = u;
        this.vertices[SpriteActor.V1] = v2;

        this.vertices[SpriteActor.X2] = x;
        this.vertices[SpriteActor.Y2] = y + height;
        this.vertices[SpriteActor.C2] = color;
        this.vertices[SpriteActor.U2] = u;
        this.vertices[SpriteActor.V2] = v;

        this.vertices[SpriteActor.X3] = x + width;
        this.vertices[SpriteActor.Y3] = y + height;
        this.vertices[SpriteActor.C3] = color;
        this.vertices[SpriteActor.U3] = u2;
        this.vertices[SpriteActor.V3] = v;

        this.vertices[SpriteActor.X4] = x + width;
        this.vertices[SpriteActor.Y4] = y;
        this.vertices[SpriteActor.C4] = color;
        this.vertices[SpriteActor.U4] = u2;
        this.vertices[SpriteActor.V4] = v2;
    }

    /**
     * Default conctructor with Region
     *
     * @param id The ID of the Actor in the scene
     * @param layer The layer ID of the renderable in the scene
     * @param textureSet The TextureSet to use
     * @param x The X coord
     * @param y The Y coord
     * @param width The width of the renderable
     * @param height The height of the renderable
     * @param regionX The X coordinate of the region
     * @param regionY The Y coordinate of the region
     * @param regionWidth The width of the region
     * @param regionHeight The height coordinate of the region
     * @param color The color modifier
     */
    public StaticActor(final int id,
                       final TextureSet textureSet,
                       final float x, final float y,
                       final float width, final float height,
                       final int regionX, final int regionY,
                       final int regionWidth, final int regionHeight,
                       final float color){
        this(id,
                textureSet,
                x, y,
                width, height,
                regionX * 1f / textureSet.getWidth(), regionY * 1f / textureSet.getHeight(),
                (regionX + regionWidth) * 1f / textureSet.getWidth(), (regionY + regionHeight) * 1f / textureSet.getHeight(),
                color
        );
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        renderer.draw(this.textureSet, this.vertices, 0, SpriteActor.SPRITE_SIZE);
    }

    /**
     * Gets the bounding rectangle of this element for touch detection
     *
     * @return The bounding Rectangle
     */
    @Override
    public Rectangle getBoundingRectangle() {
        if(this.bound == null) this.bound = new Rectangle(this.x, this.y, this.width, this.height);
        return this.bound;
    }


    /**
     * Called when the element is touched or clicked down
     *
     * @param worldX The X coordinate of the event
     * @param worldY The Y coordinate of the event
     * @param button See com.badlogic.gdx.Input.Buttons
     * @return True if the event is considered as treated and stop propagation
     */
    @Override
    public boolean onTouchDown(float worldX, float worldY, int button) {
        return false;
    }

    /**
     * Called when the element is untouched or clicked up
     *
     * @param worldX The X coordinate of the event
     * @param worldY The Y coordinate of the event
     * @param button See com.badlogic.gdx.Input.Buttons
     * @return True if the event is considered as treated and stop propagation
     */
    @Override
    public boolean onTouchUp(float worldX, float worldY, int button) {
        return false;
    }

    /**
     * Called when the element receives mouse (down) or touch move event
     *
     * @param worldX The X coordinate of the event
     * @param worldY The Y coordinate of the event
     * @return True if the event is considered as treated and stop propagation
     */
    @Override
    public boolean onTouchMove(float worldX, float worldY){
        return false;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        //NOP Texture can be shared
    }
}
