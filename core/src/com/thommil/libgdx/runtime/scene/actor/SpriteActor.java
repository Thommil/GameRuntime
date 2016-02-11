package com.thommil.libgdx.runtime.scene.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Renderable;


/**
 * Basic SpriteActor using custom SpriteBatch based on Sprite from LibGDX
 *
 * Created by thommil on 2/10/16.
 */
public class SpriteActor extends TextureRegion implements Actor, Renderable<Batch> {

    static public final int X1 = 0;
    static public final int Y1 = 1;
    static public final int U1 = 2;
    static public final int V1 = 3;
    static public final int X2 = 4;
    static public final int Y2 = 5;
    static public final int U2 = 6;
    static public final int V2 = 7;
    static public final int X3 = 8;
    static public final int Y3 = 9;
    static public final int U3 = 10;
    static public final int V3 = 11;
    static public final int X4 = 12;
    static public final int Y4 = 13;
    static public final int U4 = 14;
    static public final int V4 = 15;

    static final int VERTEX_SIZE = 2 + 2;
    static final int SPRITE_SIZE = 4 * VERTEX_SIZE;

    final float[] vertices = new float[SPRITE_SIZE];
    public float x, y;
    public float width, height;
    public float originX, originY;
    public float rotation;
    public float scaleX = 1, scaleY = 1;
    private boolean dirty = true;
    public Rectangle bounds;

    private int layer;

    /** Creates a sprite with width, height, and texture region equal to the size of the texture. */
    public SpriteActor (Texture texture) {
        this(texture, 0, 0, texture.getWidth(), texture.getHeight());
    }

    /** Creates a sprite with width, height, and texture region equal to the specified size. The texture region's upper left corner
     * will be 0,0.
     * @param srcWidth The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn. */
    public SpriteActor (Texture texture, int srcWidth, int srcHeight) {
        this(texture, 0, 0, srcWidth, srcHeight);
    }

    /** Creates a sprite with width, height, and texture region equal to the specified size.
     * @param srcWidth The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn. */
    public SpriteActor (Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        if (texture == null) throw new IllegalArgumentException("texture cannot be null.");
        this.setTexture(texture);
        setRegion(srcX, srcY, srcWidth, srcHeight);
        setSize(Math.abs(srcWidth), Math.abs(srcHeight));
        setOrigin(width / 2, height / 2);
    }

    // Note the region is copied.
    /** Creates a sprite based on a specific TextureRegion, the new sprite's region is a copy of the parameter region - altering one
     * does not affect the other */
    public SpriteActor (TextureRegion region) {
        setRegion(region);
        setSize(region.getRegionWidth(), region.getRegionHeight());
        setOrigin(width / 2, height / 2);
    }

    /** Creates a sprite with width, height, and texture region equal to the specified size, relative to specified sprite's texture
     * region.
     * @param srcWidth The width of the texture region. May be negative to flip the sprite when drawn.
     * @param srcHeight The height of the texture region. May be negative to flip the sprite when drawn. */
    public SpriteActor (TextureRegion region, int srcX, int srcY, int srcWidth, int srcHeight) {
        setRegion(region, srcX, srcY, srcWidth, srcHeight);
        setSize(Math.abs(srcWidth), Math.abs(srcHeight));
        setOrigin(width / 2, height / 2);
    }

    /** Sets the position and size of the sprite when drawn, before scaling and rotation are applied. If origin, rotation, or scale
     * are changed, it is slightly more efficient to set the bounds after those operations. */
    public void setBounds (float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (dirty) return;

        float x2 = x + width;
        float y2 = y + height;
        float[] vertices = this.vertices;
        vertices[X1] = x;
        vertices[Y1] = y;

        vertices[X2] = x;
        vertices[Y2] = y2;

        vertices[X3] = x2;
        vertices[Y3] = y2;

        vertices[X4] = x2;
        vertices[Y4] = y;

        if (rotation != 0 || scaleX != 1 || scaleY != 1) dirty = true;
    }

    /** Sets the size of the sprite when drawn, before scaling and rotation are applied. If origin, rotation, or scale are changed,
     * it is slightly more efficient to set the size after those operations. If both position and size are to be changed, it is
     * better to use {@link #setBounds(float, float, float, float)}. */
    public void setSize (float width, float height) {
        this.width = width;
        this.height = height;

        if (dirty) return;

        float x2 = x + width;
        float y2 = y + height;
        float[] vertices = this.vertices;
        vertices[X1] = x;
        vertices[Y1] = y;

        vertices[X2] = x;
        vertices[Y2] = y2;

        vertices[X3] = x2;
        vertices[Y3] = y2;

        vertices[X4] = x2;
        vertices[Y4] = y;

        if (rotation != 0 || scaleX != 1 || scaleY != 1) dirty = true;
    }

    /** Sets the position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
     * to set the position after those operations. If both position and size are to be changed, it is better to use
     * {@link #setBounds(float, float, float, float)}. */
    public void setPosition (float x, float y) {
        translate(x - this.x, y - this.y);
    }

    /** Sets the x position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
     * to set the position after those operations. If both position and size are to be changed, it is better to use
     * {@link #setBounds(float, float, float, float)}. */
    public void setX (float x) {
        translateX(x - this.x);
    }

    /** Sets the y position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
     * to set the position after those operations. If both position and size are to be changed, it is better to use
     * {@link #setBounds(float, float, float, float)}. */
    public void setY (float y) {
        translateY(y - this.y);
    }

    /** Sets the x position so that it is centered on the given x parameter */
    public void setCenterX(float x){
        setX(x - width / 2);
    }

    /** Sets the y position so that it is centered on the given y parameter */
    public void setCenterY(float y){
        setY(y - height / 2);
    }

    /** Sets the position so that the sprite is centered on (x, y) */
    public void setCenter(float x, float y){
        setCenterX(x);
        setCenterY(y);
    }

    /** Sets the x position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
     * changed, it is slightly more efficient to translate after those operations. */
    public void translateX (float xAmount) {
        this.x += xAmount;

        if (dirty) return;

        float[] vertices = this.vertices;
        vertices[X1] += xAmount;
        vertices[X2] += xAmount;
        vertices[X3] += xAmount;
        vertices[X4] += xAmount;
    }

    /** Sets the y position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
     * changed, it is slightly more efficient to translate after those operations. */
    public void translateY (float yAmount) {
        y += yAmount;

        if (dirty) return;

        float[] vertices = this.vertices;
        vertices[Y1] += yAmount;
        vertices[Y2] += yAmount;
        vertices[Y3] += yAmount;
        vertices[Y4] += yAmount;
    }

    /** Sets the position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
     * changed, it is slightly more efficient to translate after those operations. */
    public void translate (float xAmount, float yAmount) {
        x += xAmount;
        y += yAmount;

        if (dirty) return;

        float[] vertices = this.vertices;
        vertices[X1] += xAmount;
        vertices[Y1] += yAmount;

        vertices[X2] += xAmount;
        vertices[Y2] += yAmount;

        vertices[X3] += xAmount;
        vertices[Y3] += yAmount;

        vertices[X4] += xAmount;
        vertices[Y4] += yAmount;
    }

    /** Sets the alpha portion of the color used to tint this sprite. */
    public void setAlpha (float a) {
        //TODO
    }

    /** Sets the origin in relation to the sprite's position for scaling and rotation. */
    public void setOrigin (float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
        dirty = true;
    }

    /** Place origin in the center of the sprite */
    public void setOriginCenter() {
        this.originX = width / 2;
        this.originY = height / 2;
        dirty = true;
    }

    /** Sets the rotation of the sprite in degrees. Rotation is centered on the origin set in {@link #setOrigin(float, float)} */
    public void setRotation (float degrees) {
        this.rotation = degrees;
        dirty = true;
    }

    /** @return the rotation of the sprite in degrees */
    public float getRotation () {
        return rotation;
    }

    /** Sets the sprite's rotation in degrees relative to the current rotation. Rotation is centered on the origin set in
     * {@link #setOrigin(float, float)} */
    public void rotate (float degrees) {
        if (degrees == 0) return;
        rotation += degrees;
        dirty = true;
    }

    /** Rotates this sprite 90 degrees in-place by rotating the texture coordinates. This rotation is unaffected by
     * {@link #setRotation(float)} and {@link #rotate(float)}. */
    public void rotate90 (boolean clockwise) {
        float[] vertices = this.vertices;

        if (clockwise) {
            float temp = vertices[V1];
            vertices[V1] = vertices[V4];
            vertices[V4] = vertices[V3];
            vertices[V3] = vertices[V2];
            vertices[V2] = temp;

            temp = vertices[U1];
            vertices[U1] = vertices[U4];
            vertices[U4] = vertices[U3];
            vertices[U3] = vertices[U2];
            vertices[U2] = temp;
        } else {
            float temp = vertices[V1];
            vertices[V1] = vertices[V2];
            vertices[V2] = vertices[V3];
            vertices[V3] = vertices[V4];
            vertices[V4] = temp;

            temp = vertices[U1];
            vertices[U1] = vertices[U2];
            vertices[U2] = vertices[U3];
            vertices[U3] = vertices[U4];
            vertices[U4] = temp;
        }
    }

    /** Sets the sprite's scale for both X and Y uniformly.*/
    public void setScale (float scaleXY) {
        this.scaleX = scaleXY;
        this.scaleY = scaleXY;
        dirty = true;
    }

    /** Sets the sprite's scale for both X and Y.*/
    public void setScale (float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        dirty = true;
    }

    /** Sets the sprite's scale relative to the current scale. for example: original scale 2 -> sprite.scale(4) -> final scale 6.
     * The sprite scales out from the origin.
    public void scale (float amount) {
        this.scaleX += amount;
        this.scaleY += amount;
        dirty = true;
    }

    /** Returns the packed vertices, colors, and texture coordinates for this sprite. */
    public float[] getVertices () {
        if (dirty) {
            dirty = false;

            float[] vertices = this.vertices;
            float localX = -originX;
            float localY = -originY;
            float localX2 = localX + width;
            float localY2 = localY + height;
            float worldOriginX = this.x - localX;
            float worldOriginY = this.y - localY;
            if (scaleX != 1 || scaleY != 1) {
                localX *= scaleX;
                localY *= scaleY;
                localX2 *= scaleX;
                localY2 *= scaleY;
            }
            if (rotation != 0) {
                final float cos = MathUtils.cosDeg(rotation);
                final float sin = MathUtils.sinDeg(rotation);
                final float localXCos = localX * cos;
                final float localXSin = localX * sin;
                final float localYCos = localY * cos;
                final float localYSin = localY * sin;
                final float localX2Cos = localX2 * cos;
                final float localX2Sin = localX2 * sin;
                final float localY2Cos = localY2 * cos;
                final float localY2Sin = localY2 * sin;

                final float x1 = localXCos - localYSin + worldOriginX;
                final float y1 = localYCos + localXSin + worldOriginY;
                vertices[X1] = x1;
                vertices[Y1] = y1;

                final float x2 = localXCos - localY2Sin + worldOriginX;
                final float y2 = localY2Cos + localXSin + worldOriginY;
                vertices[X2] = x2;
                vertices[Y2] = y2;

                final float x3 = localX2Cos - localY2Sin + worldOriginX;
                final float y3 = localY2Cos + localX2Sin + worldOriginY;
                vertices[X3] = x3;
                vertices[Y3] = y3;

                vertices[X4] = x1 + (x3 - x2);
                vertices[Y4] = y3 - (y2 - y1);
            } else {
                final float x1 = localX + worldOriginX;
                final float y1 = localY + worldOriginY;
                final float x2 = localX2 + worldOriginX;
                final float y2 = localY2 + worldOriginY;

                vertices[X1] = x1;
                vertices[Y1] = y1;

                vertices[X2] = x1;
                vertices[Y2] = y2;

                vertices[X3] = x2;
                vertices[Y3] = y2;

                vertices[X4] = x2;
                vertices[Y4] = y1;
            }
        }
        return vertices;
    }

    /** Returns the bounding axis aligned {@link Rectangle} that bounds this sprite. The rectangles x and y coordinates describe its
     * bottom left corner. If you change the position or size of the sprite, you have to fetch the triangle again for it to be
     * recomputed.
     *
     * @return the bounding Rectangle */
    public Rectangle getBoundingRectangle () {
        final float[] vertices = getVertices();

        float minx = vertices[X1];
        float miny = vertices[Y1];
        float maxx = vertices[X1];
        float maxy = vertices[Y1];

        minx = minx > vertices[X2] ? vertices[X2] : minx;
        minx = minx > vertices[X3] ? vertices[X3] : minx;
        minx = minx > vertices[X4] ? vertices[X4] : minx;

        maxx = maxx < vertices[X2] ? vertices[X2] : maxx;
        maxx = maxx < vertices[X3] ? vertices[X3] : maxx;
        maxx = maxx < vertices[X4] ? vertices[X4] : maxx;

        miny = miny > vertices[Y2] ? vertices[Y2] : miny;
        miny = miny > vertices[Y3] ? vertices[Y3] : miny;
        miny = miny > vertices[Y4] ? vertices[Y4] : miny;

        maxy = maxy < vertices[Y2] ? vertices[Y2] : maxy;
        maxy = maxy < vertices[Y3] ? vertices[Y3] : maxy;
        maxy = maxy < vertices[Y4] ? vertices[Y4] : maxy;

        if (bounds == null) bounds = new Rectangle();
        bounds.x = minx;
        bounds.y = miny;
        bounds.width = maxx - minx;
        bounds.height = maxy - miny;
        return bounds;
    }


    public void setRegion (float u, float v, float u2, float v2) {
        super.setRegion(u, v, u2, v2);

        float[] vertices = this.vertices;
        vertices[U1] = u;
        vertices[V1] = v2;

        vertices[U2] = u;
        vertices[V2] = v;

        vertices[U3] = u2;
        vertices[V3] = v;

        vertices[U4] = u2;
        vertices[V4] = v2;
    }

    public void setU (float u) {
        super.setU(u);
        vertices[U1] = u;
        vertices[U2] = u;
    }

    public void setV (float v) {
        super.setV(v);
        vertices[V2] = v;
        vertices[V3] = v;
    }

    public void setU2 (float u2) {
        super.setU2(u2);
        vertices[U3] = u2;
        vertices[U4] = u2;
    }

    public void setV2 (float v2) {
        super.setV2(v2);
        vertices[V1] = v2;
        vertices[V4] = v2;
    }

    /** Set the sprite's flip state regardless of current condition
     * @param x the desired horizontal flip state
     * @param y the desired vertical flip state */
    public void setFlip (boolean x, boolean y) {
        boolean performX = false;
        boolean performY = false;
        if (isFlipX() != x) {
            performX = true;
        }
        if (isFlipY() != y) {
            performY = true;
        }
        flip(performX, performY);
    }

    /** boolean parameters x,y are not setting a state, but performing a flip
     * @param x perform horizontal flip
     * @param y perform vertical flip */
    public void flip (boolean x, boolean y) {
        super.flip(x, y);
        float[] vertices = this.vertices;
        if (x) {
            float temp = vertices[U1];
            vertices[U1] = vertices[U3];
            vertices[U3] = temp;
            temp = vertices[U2];
            vertices[U2] = vertices[U4];
            vertices[U4] = temp;
        }
        if (y) {
            float temp = vertices[V1];
            vertices[V1] = vertices[V3];
            vertices[V3] = temp;
            temp = vertices[V2];
            vertices[V2] = vertices[V4];
            vertices[V4] = temp;
        }
    }

    public void scroll (float xAmount, float yAmount) {
        float[] vertices = this.vertices;
        if (xAmount != 0) {
            float u = (vertices[U1] + xAmount) % 1;
            float u2 = u + width / this.getTexture().getWidth();
            this.setU(u);
            this.setU2(u2);
            vertices[U1] = u;
            vertices[U2] = u;
            vertices[U3] = u2;
            vertices[U4] = u2;
        }
        if (yAmount != 0) {
            float v = (vertices[V2] + yAmount) % 1;
            float v2 = v + height / this.getTexture().getHeight();
            this.setV(v);
            this.setV2(v2);
            vertices[V1] = v2;
            vertices[V2] = v;
            vertices[V3] = v;
            vertices[V4] = v2;
        }
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
        renderer.draw(this.getTexture(), getVertices(), 0, SPRITE_SIZE);
    }

    @Override
    public void dispose() {
        this.getTexture().dispose();
    }
}
