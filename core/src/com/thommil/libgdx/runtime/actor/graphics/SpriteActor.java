package com.thommil.libgdx.runtime.actor.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.NumberUtils;
import com.thommil.libgdx.runtime.events.TouchListener;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.graphics.animation.TextureRegionAnimation;
import com.thommil.libgdx.runtime.graphics.renderer.sprite.SpriteBatchRenderer;
import com.thommil.libgdx.runtime.actor.Actor;


/**
 * Basic SpriteActor for BasicBatch rendering based on Sprite from LibGDX
 *
 * @author thommil on 03/02/16.
 */
public class SpriteActor extends Actor implements Renderable<SpriteBatchRenderer>, TouchListener {

    static public final int X1 = 0;
    static public final int Y1 = 1;
    static public final int C1 = 2;
    static public final int U1 = 3;
    static public final int V1 = 4;

    static public final int X2 = 5;
    static public final int Y2 = 6;
    static public final int C2 = 7;
    static public final int U2 = 8;
    static public final int V2 = 9;

    static public final int X3 = 10;
    static public final int Y3 = 11;
    static public final int C3 = 12;
    static public final int U3 = 13;
    static public final int V3 = 14;

    static public final int X4 = 15;
    static public final int Y4 = 16;
    static public final int C4 = 17;
    static public final int U4 = 18;
    static public final int V4 = 19;

    public static final int VERTEX_SIZE = 2 + 1 + 2;
    public static final int SPRITE_SIZE = 4 * VERTEX_SIZE;

    public TextureSet textureSet;
    public float x, y;
    public float u, v;
    public float u2, v2;
    public float width, height;
    public float originX, originY;
    public float rotation;
    public float scaleX = 1, scaleY = 1;
    public float color;
    public  Rectangle bounds;

    protected int regionWidth, regionHeight;
    protected final float[] vertices = new float[SPRITE_SIZE];
    protected boolean dirty = true;

    public SpriteActor (final int id, final TextureSet textureSet) {
        this(id, textureSet, 0, 0, textureSet.getWidth(), textureSet.getHeight(), Math.abs(textureSet.getWidth()), Math.abs(textureSet.getHeight()));
    }
    public SpriteActor (final int id, final TextureSet textureSet, float width, float height) {
        this(id, textureSet, 0, 0, textureSet.getWidth(), textureSet.getHeight(), width, height);
    }

    public SpriteActor (final int id, final TextureSet textureSet, final int srcWidth, final int srcHeight) {
        this(id, textureSet, 0, 0, srcWidth, srcHeight, Math.abs(srcWidth), Math.abs(srcHeight));
    }

    public SpriteActor (final int id, final TextureSet textureSet, final int srcWidth, final int srcHeight, float width, float height) {
        this(id, textureSet, 0, 0, srcWidth, srcHeight, width, height);
    }

    public SpriteActor (final int id, final TextureSet textureSet, final int srcX, final int srcY, final int srcWidth, final int srcHeight) {
        this(id, textureSet, srcX, srcY, srcWidth, srcHeight, Math.abs(srcWidth), Math.abs(srcHeight));
    }

    public SpriteActor (final int id, final TextureSet textureSet, final int srcX, final int srcY, final int srcWidth, final int srcHeight, float width, float height){
        super(id);
        if (textureSet == null) throw new IllegalArgumentException("texture cannot be null.");
        this.textureSet = textureSet;
        this.setColor(Color.WHITE.toFloatBits());
        this.setRegion(srcX, srcY, srcWidth, srcHeight);
        this.setSize(width, height);
        this.setOrigin(width / 2, height / 2);
        this.setPosition(0,0);
    }

    /** Sets the position and size of the sprite when drawn, before scaling and rotation are applied. If origin, rotation, or scale
     * are changed, it is slightly more efficient to set the bounds after those operations. */
    public void setBounds (float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if (this.dirty) return;

        final float x2 = x + width;
        final float y2 = y + height;
        this.vertices[X1] = x;
        this.vertices[Y1] = y;

        this.vertices[X2] = x;
        this.vertices[Y2] = y2;

        this.vertices[X3] = x2;
        this.vertices[Y3] = y2;

        this.vertices[X4] = x2;
        this.vertices[Y4] = y;

        if (this.rotation != 0 || this.scaleX != 1 || this.scaleY != 1) this.dirty = true;
    }

    /** Sets the size of the sprite when drawn, before scaling and rotation are applied. If origin, rotation, or scale are changed,
     * it is slightly more efficient to set the size after those operations. If both position and size are to be changed, it is
     * better to use {@link #setBounds(float, float, float, float)}. */
    public void setSize (float width, float height) {
        this.width = width;
        this.height = height;

        if (this.dirty) return;

        final float x2 = this.x + width;
        final float y2 = this.y + height;
        this.vertices[X1] = this.x;
        this.vertices[Y1] = this.y;

        this.vertices[X2] = this.x;
        this.vertices[Y2] = y2;

        this.vertices[X3] = x2;
        this.vertices[Y3] = y2;

        this.vertices[X4] = x2;
        this.vertices[Y4] = this.y;

        if (this.rotation != 0 || this.scaleX != 1 || this.scaleY != 1) this.dirty = true;
    }

    /** Sets the position where the sprite will be drawn. If origin, rotation, or scale are changed, it is slightly more efficient
     * to set the position after those operations. If both position and size are to be changed, it is better to use
     * {@link #setBounds(float, float, float, float)}. */
    public void setPosition (float x, float y) {
        translate(x - this.originX - this.x, y - this.originY - this.y);
    }

    /** Sets the position relative to the current position where the sprite will be drawn. If origin, rotation, or scale are
     * changed, it is slightly more efficient to translate after those operations. */
    public void translate (float xAmount, float yAmount) {
        this.x += xAmount;
        this.y += yAmount;

        if (this.dirty) return;

        this.vertices[X1] += xAmount;
        this.vertices[Y1] += yAmount;

        this.vertices[X2] += xAmount;
        this.vertices[Y2] += yAmount;

        this.vertices[X3] += xAmount;
        this.vertices[Y3] += yAmount;

        this.vertices[X4] += xAmount;
        this.vertices[Y4] += yAmount;
    }

    /** Sets the origin in relation to the sprite's position for scaling and rotation. */
    public void setOrigin (float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
        this.dirty = true;
    }

    /** Place origin in the center of the sprite */
    public void setOriginCenter() {
        this.originX = this.width / 2;
        this.originY = this.height / 2;
        this.dirty = true;
    }

    /** Sets the rotation of the sprite in degrees. Rotation is centered on the origin set in {@link #setOrigin(float, float)} */
    public void setRotation (float degrees) {
        this.rotation = degrees;
        this.dirty = true;
    }

    /** Sets the rotation of the sprite in radiants. Rotation is centered on the origin set in {@link #setOrigin(float, float)} */
    public void setRotationRad (float radiants) {
        this.rotation = radiants * MathUtils.radDeg;
        this.dirty = true;
    }

    /** @return the rotation of the sprite in degrees */
    public float getRotation () {
        return this.rotation;
    }

    /** @return the rotation of the sprite in radiants */
    public float getRotationRad () {
        return this.rotation / MathUtils.radDeg;
    }

    /** Sets the sprite's rotation in degrees relative to the current rotation. Rotation is centered on the origin set in
     * {@link #setOrigin(float, float)} */
    public void rotate (float degrees) {
        if (degrees == 0) return;
        this.rotation += degrees;
        this.dirty = true;
    }

    /** Sets the sprite's rotation in radiants relative to the current rotation. Rotation is centered on the origin set in
     * {@link #setOrigin(float, float)} */
    public void rotateRad (float radiants) {
        if (radiants == 0) return;
        this.rotation += radiants * MathUtils.radDeg;
        this.dirty = true;
    }

    /** Sets the sprite's scale for both X and Y uniformly. The sprite scales out from the origin. */
    public void setScale (float scaleXY) {
        this.scaleX = scaleXY;
        this.scaleY = scaleXY;
        this.dirty = true;
    }

    /** Sets the sprite's scale for both X and Y. The sprite scales out from the origin.*/
    public void setScale (float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.dirty = true;
    }

    /** Sets the sprite's scale relative to the current scale. for example: original scale 2 -> sprite.scale(4) -> final scale 6.
     * The sprite scales out from the origin.*/
    public void scale (float amount) {
        this.scaleX += amount;
        this.scaleY += amount;
        this.dirty = true;
    }

    /** Returns the packed vertices, colors, and texture coordinates for this sprite. */
    public float[] getVertices () {
        if (this.dirty) {
            this.dirty = false;

            float localX = -this.originX;
            float localY = -this.originY;
            float localX2 = localX + this.width;
            float localY2 = localY + this.height;
            final float worldOriginX = this.x - localX;
            final float worldOriginY = this.y - localY;
            if (this.scaleX != 1 || this.scaleY != 1) {
                localX *= this.scaleX;
                localY *= this.scaleY;
                localX2 *= this.scaleX;
                localY2 *= this.scaleY;
            }
            if (this.rotation != 0) {
                final float cos = MathUtils.cosDeg(this.rotation);
                final float sin = MathUtils.sinDeg(this.rotation);
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
                this.vertices[X1] = x1;
                this.vertices[Y1] = y1;

                final float x2 = localXCos - localY2Sin + worldOriginX;
                final float y2 = localY2Cos + localXSin + worldOriginY;
                this.vertices[X2] = x2;
                this.vertices[Y2] = y2;

                final float x3 = localX2Cos - localY2Sin + worldOriginX;
                final float y3 = localY2Cos + localX2Sin + worldOriginY;
                this.vertices[X3] = x3;
                this.vertices[Y3] = y3;

                this.vertices[X4] = x1 + (x3 - x2);
                this.vertices[Y4] = y3 - (y2 - y1);
            } else {
                final float x1 = localX + worldOriginX;
                final float y1 = localY + worldOriginY;
                final float x2 = localX2 + worldOriginX;
                final float y2 = localY2 + worldOriginY;

                this.vertices[X1] = x1;
                this.vertices[Y1] = y1;

                this.vertices[X2] = x1;
                this.vertices[Y2] = y2;

                this.vertices[X3] = x2;
                this.vertices[Y3] = y2;

                this.vertices[X4] = x2;
                this.vertices[Y4] = y1;
            }
        }
        return this.vertices;
    }

    public void setRegion (float u, float v, float u2, float v2) {
        final int texWidth = this.textureSet.getWidth(), texHeight = this.textureSet.getHeight();
        this.regionWidth = Math.round(Math.abs(u2 - u) * texWidth);
        this.regionHeight = Math.round(Math.abs(v2 - v) * texHeight);

        // For a 1x1 region, adjust UVs toward pixel center to avoid filtering artifacts on AMD GPUs when drawing very stretched.
        if (this.regionWidth == 1 && this.regionHeight == 1) {
            final float adjustX = 0.25f / texWidth;
            u += adjustX;
            u2 -= adjustX;
            final float adjustY = 0.25f / texHeight;
            v += adjustY;
            v2 -= adjustY;
        }

        this.u = u;
        this.v = v;
        this.u2 = u2;
        this.v2 = v2;

        this.vertices[U1] = u;
        this.vertices[V1] = v2;

        this.vertices[U2] = u;
        this.vertices[V2] = v;

        this.vertices[U3] = u2;
        this.vertices[V3] = v;

        this.vertices[U4] = u2;
        this.vertices[V4] = v2;
    }

    public void setU (float u) {
        this.u = u;
        this.regionWidth = Math.round(Math.abs(this.u2 - u) * this.textureSet.getWidth());
        this.vertices[U1] = u;
        this.vertices[U2] = u;
    }

    public void setV (float v) {
        this.v = v;
        this.regionHeight = Math.round(Math.abs(this.v2 - v) * this.textureSet.getHeight());
        this.vertices[V2] = v;
        this.vertices[V3] = v;
    }

    public void setU2 (float u2) {
        this.u2 = u2;
        this.regionWidth = Math.round(Math.abs(u2 - this.u) * this.textureSet.getWidth());
        this.vertices[U3] = u2;
        this.vertices[U4] = u2;
    }

    public void setV2 (float v2) {
        this.v2 = v2;
        this.regionHeight = Math.round(Math.abs(v2 - this.v) * this.textureSet.getHeight());
        this.vertices[V1] = v2;
        this.vertices[V4] = v2;
    }

    /** Set the sprite's flip state regardless of current condition
     * @param x the desired horizontal flip state
     * @param y the desired vertical flip state */
    public void setFlip (boolean x, boolean y) {
        flip((isFlipX() != x), (isFlipY() != y));
    }

    /** boolean parameters x,y are not setting a state, but performing a flip
     * @param x perform horizontal flip
     * @param y perform vertical flip */
    public void flip (boolean x, boolean y) {
        if (x) {
            float temp = this.u;
            this.u = this.u2;
            this.u2 = temp;
        }
        if (y) {
            float temp = this.v;
            this.v = this.v2;
            this.v2 = temp;
        }

        if (x) {
            float temp = this.vertices[U1];
            this.vertices[U1] = this.vertices[U3];
            this.vertices[U3] = temp;
            temp = this.vertices[U2];
            this.vertices[U2] = this.vertices[U4];
            this.vertices[U4] = temp;
        }
        if (y) {
            float temp = this.vertices[V1];
            this.vertices[V1] = this.vertices[V3];
            this.vertices[V3] = temp;
            temp = this.vertices[V2];
            this.vertices[V2] = this.vertices[V4];
            this.vertices[V4] = temp;
        }
    }

    public void scroll (float xAmount, float yAmount) {
        if (xAmount != 0) {
            final float u = (this.vertices[U1] + xAmount) % 1;
            final float u2 = u + this.width / this.textureSet.getWidth();
            this.u = u;
            this.u2 = u2;
            this.vertices[U1] = u;
            this.vertices[U2] = u;
            this.vertices[U3] = u2;
            this.vertices[U4] = u2;
        }
        if (yAmount != 0) {
            final float v = (this.vertices[V2] + yAmount) % 1;
            final float v2 = v + this.height / this.textureSet.getHeight();
            this.v = v;
            this.v2 = v2;
            this.vertices[V1] = v2;
            this.vertices[V2] = v;
            this.vertices[V3] = v;
            this.vertices[V4] = v2;
        }
    }

    /** @param width The width of the texture region. May be negative to flip the sprite when drawn.
     * @param height The height of the texture region. May be negative to flip the sprite when drawn. */
    public void setRegion (int x, int y, int width, int height) {
        final float invTexWidth = 1f / this.textureSet.getWidth();
        final float invTexHeight = 1f / this.textureSet.getHeight();
        this.setRegion(x * invTexWidth, y * invTexHeight, (x + width) * invTexWidth, (y + height) * invTexHeight);
        this.regionWidth = Math.abs(width);
        this.regionHeight = Math.abs(height);
    }

    public boolean isFlipX () {
        return this.u > this.u2;
    }

    public boolean isFlipY () {
        return this.v > this.v2;
    }

    public void setColor (Color color) {
        this.setColor(color.toFloatBits());
    }

    public void setColor (float r, float g, float b, float a) {
        int intBits = (int)(255 * a) << 24 | (int)(255 * b) << 16 | (int)(255 * g) << 8 | (int)(255 * r);
        this.setColor(NumberUtils.intToFloatColor(intBits));
    }

    public void setColor (float color) {
        this.color = color;
        this.vertices[C1] = color;
        this.vertices[C2] = color;
        this.vertices[C3] = color;
        this.vertices[C4] = color;
    }

    public Color getColor () {
        final int intBits = NumberUtils.floatToIntColor(color);
        Color color = new Color();
        color.r = (intBits & 0xff) / 255f;
        color.g = ((intBits >>> 8) & 0xff) / 255f;
        color.b = ((intBits >>> 16) & 0xff) / 255f;
        color.a = ((intBits >>> 24) & 0xff) / 255f;
        return color;
    }

    /**
     * Gets the ID of the Actor
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Render the element on current viewport (do access physics world here !)
     *
     * @param deltaTime The delta time since last call
     * @param renderer  The renderer to use in current layer
     */
    @Override
    public void render(float deltaTime, SpriteBatchRenderer renderer) {
        renderer.draw(this.textureSet, this.getVertices(), 0, SPRITE_SIZE);
    }

    /**
     * Gets the bounding rectangle of this element for touch detection
     *
     * @return The bounding Rectangle
     */
    @Override
    public Rectangle getBoundingRectangle() {
        float minx = this.vertices[X1];
        float miny = this.vertices[Y1];
        float maxx = this.vertices[X1];
        float maxy = this.vertices[Y1];

        minx = minx > this.vertices[X2] ? this.vertices[X2] : minx;
        minx = minx > this.vertices[X3] ? this.vertices[X3] : minx;
        minx = minx > this.vertices[X4] ? this.vertices[X4] : minx;

        maxx = maxx < this.vertices[X2] ? this.vertices[X2] : maxx;
        maxx = maxx < this.vertices[X3] ? this.vertices[X3] : maxx;
        maxx = maxx < this.vertices[X4] ? this.vertices[X4] : maxx;

        miny = miny > this.vertices[Y2] ? this.vertices[Y2] : miny;
        miny = miny > this.vertices[Y3] ? this.vertices[Y3] : miny;
        miny = miny > this.vertices[Y4] ? this.vertices[Y4] : miny;

        maxy = maxy < this.vertices[Y2] ? this.vertices[Y2] : maxy;
        maxy = maxy < this.vertices[Y3] ? this.vertices[Y3] : maxy;
        maxy = maxy < this.vertices[Y4] ? this.vertices[Y4] : maxy;

        if (this.bounds == null) this.bounds = new Rectangle();
        this.bounds.x = minx;
        this.bounds.y = miny;
        this.bounds.width = maxx - minx;
        this.bounds.height = maxy - miny;
        return this.bounds;
    }

    /**
     * Play a given animation in current sprite actor at specified state time
     *
     * @param animation The animation to use
     * @param stateTime The state time in seconds
     */
    public SpriteActor playAnimation(final TextureRegionAnimation animation, final float stateTime){
        final TextureRegion textureRegion = animation.getKeyFrame(stateTime);
        this.setRegion(textureRegion.getU(), textureRegion.getV(), textureRegion.getU2(), textureRegion.getV2());
        return this;
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
