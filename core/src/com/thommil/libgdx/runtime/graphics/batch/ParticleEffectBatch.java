package com.thommil.libgdx.runtime.graphics.batch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;

/**
 * Created by tomtom on 14/02/16.
 */
public class ParticleEffectBatch implements Batch {

    /**
     * Sets up the Batch for drawing. This will disable depth buffer writing. It enables blending and texturing. If you have more
     * texture units enabled than the first one you have to disable them before calling this. Uses a screen coordinate system by
     * default where everything is given in pixels. You can specify your own projection and modelview matrices via
     * {@link #setProjectionMatrix(Matrix4)} and {@link #setTransformMatrix(Matrix4)}.
     */
    @Override
    public void begin() {

    }

    /**
     * Finishes off rendering. Enables depth writes, disables blending and texturing. Must always be called after a call to
     * {@link #begin()}
     */
    @Override
    public void end() {

    }

    /**
     * Sets the color used to tint images when they are added to the Batch. Default is {@link Color#WHITE}.
     *
     * @param tint
     */
    @Override
    public void setColor(Color tint) {

    }

    /**
     * @param r
     * @param g
     * @param b
     * @param a
     * @see #setColor(Color)
     */
    @Override
    public void setColor(float r, float g, float b, float a) {

    }

    /**
     * @param color
     * @see #setColor(Color)
     * @see Color#toFloatBits()
     */
    @Override
    public void setColor(float color) {

    }

    /**
     * @return the rendering color of this Batch. Manipulating the returned instance has no effect.
     */
    @Override
    public Color getColor() {
        return null;
    }

    /**
     * @return the rendering color of this Batch in vertex format
     * @see Color#toFloatBits()
     */
    @Override
    public float getPackedColor() {
        return 0;
    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The rectangle is offset by
     * originX, originY relative to the origin. Scale specifies the scaling factor by which the rectangle should be scaled around
     * originX, originY. Rotation specifies the angle of counter clockwise rotation of the rectangle around originX, originY. The
     * portion of the {@link Texture} given by srcX, srcY and srcWidth, srcHeight is used. These coordinates and sizes are given in
     * texels. FlipX and flipY specify whether the texture portion should be flipped horizontally or vertically.
     *
     * @param texture
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param originX   the x-coordinate of the scaling and rotation origin relative to the screen space coordinates
     * @param originY   the y-coordinate of the scaling and rotation origin relative to the screen space coordinates
     * @param width     the width in pixels
     * @param height    the height in pixels
     * @param scaleX    the scale of the rectangle around originX/originY in x
     * @param scaleY    the scale of the rectangle around originX/originY in y
     * @param rotation  the angle of counter clockwise rotation of the rectangle around originX/originY
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     * @param flipX     whether to flip the sprite horizontally
     * @param flipY     whether to flip the sprite vertically
     */
    @Override
    public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {

    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture} given by srcX, srcY and srcWidth, srcHeight is used. These coordinates and sizes are given in texels. FlipX
     * and flipY specify whether the texture portion should be flipped horizontally or vertically.
     *
     * @param texture
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param width     the width in pixels
     * @param height    the height in pixels
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     * @param flipX     whether to flip the sprite horizontally
     * @param flipY     whether to flip the sprite vertically
     */
    @Override
    public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {

    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture} given by srcX, srcY and srcWidth, srcHeight are used. These coordinates and sizes are given in texels.
     *
     * @param texture
     * @param x         the x-coordinate in screen space
     * @param y         the y-coordinate in screen space
     * @param srcX      the x-coordinate in texel space
     * @param srcY      the y-coordinate in texel space
     * @param srcWidth  the source with in texels
     * @param srcHeight the source height in texels
     */
    @Override
    public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {

    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the given width and height in pixels. The portion of the
     * {@link Texture} given by u, v and u2, v2 are used. These coordinates and sizes are given in texture size percentage. The
     * rectangle will have the given tint {@link Color}.
     *
     * @param texture
     * @param x       the x-coordinate in screen space
     * @param y       the y-coordinate in screen space
     * @param width   the width in pixels
     * @param height  the height in pixels
     * @param u
     * @param v
     * @param u2
     * @param v2
     */
    @Override
    public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {

    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the width and height of the texture.
     *
     * @param texture
     * @param x       the x-coordinate in screen space
     * @param y       the y-coordinate in screen space
     */
    @Override
    public void draw(Texture texture, float x, float y) {

    }

    /**
     * Draws a rectangle with the bottom left corner at x,y and stretching the region to cover the given width and height.
     *
     * @param texture
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void draw(Texture texture, float x, float y, float width, float height) {

    }

    /**
     * Draws a rectangle using the given vertices. There must be 4 vertices, each made up of 5 elements in this order: x, y, color,
     * u, v. The {@link #getColor()} from the Batch is not applied.
     *
     * @param texture
     * @param spriteVertices
     * @param offset
     * @param count
     */
    @Override
    public void draw(Texture texture, float[] spriteVertices, int offset, int count) {

    }

    /**
     * Draws a rectangle with the bottom left corner at x,y having the width and height of the region.
     *
     * @param region
     * @param x
     * @param y
     */
    @Override
    public void draw(TextureRegion region, float x, float y) {

    }

    /**
     * Draws a rectangle with the bottom left corner at x,y and stretching the region to cover the given width and height.
     *
     * @param region
     * @param x
     * @param y
     * @param width
     * @param height
     */
    @Override
    public void draw(TextureRegion region, float x, float y, float width, float height) {

    }

    /**
     * Draws a rectangle with the bottom left corner at x,y and stretching the region to cover the given width and height. The
     * rectangle is offset by originX, originY relative to the origin. Scale specifies the scaling factor by which the rectangle
     * should be scaled around originX, originY. Rotation specifies the angle of counter clockwise rotation of the rectangle around
     * originX, originY.
     *
     * @param region
     * @param x
     * @param y
     * @param originX
     * @param originY
     * @param width
     * @param height
     * @param scaleX
     * @param scaleY
     * @param rotation
     */
    @Override
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {

    }

    /**
     * Draws a rectangle with the texture coordinates rotated 90 degrees. The bottom left corner at x,y and stretching the region
     * to cover the given width and height. The rectangle is offset by originX, originY relative to the origin. Scale specifies the
     * scaling factor by which the rectangle should be scaled around originX, originY. Rotation specifies the angle of counter
     * clockwise rotation of the rectangle around originX, originY.
     *
     * @param region
     * @param x
     * @param y
     * @param originX
     * @param originY
     * @param width
     * @param height
     * @param scaleX
     * @param scaleY
     * @param rotation
     * @param clockwise If true, the texture coordinates are rotated 90 degrees clockwise. If false, they are rotated 90 degrees
     */
    @Override
    public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {

    }

    /**
     * Draws a rectangle transformed by the given matrix.
     *
     * @param region
     * @param width
     * @param height
     * @param transform
     */
    @Override
    public void draw(TextureRegion region, float width, float height, Affine2 transform) {

    }

    /**
     * Causes any pending sprites to be rendered, without ending the Batch.
     */
    @Override
    public void flush() {

    }

    /**
     * Disables blending for drawing sprites. Calling this within {@link #begin()}/{@link #end()} will flush the batch.
     */
    @Override
    public void disableBlending() {

    }

    /**
     * Enables blending for drawing sprites. Calling this within {@link #begin()}/{@link #end()} will flush the batch.
     */
    @Override
    public void enableBlending() {

    }

    /**
     * Sets the blending function to be used when rendering sprites.
     *
     * @param srcFunc the source function, e.g. GL20.GL_SRC_ALPHA. If set to -1, Batch won't change the blending function.
     * @param dstFunc the destination function, e.g. GL20.GL_ONE_MINUS_SRC_ALPHA
     */
    @Override
    public void setBlendFunction(int srcFunc, int dstFunc) {

    }

    @Override
    public int getBlendSrcFunc() {
        return 0;
    }

    @Override
    public int getBlendDstFunc() {
        return 0;
    }

    /**
     * Returns the current projection matrix. Changing this within {@link #begin()}/{@link #end()} results in undefined behaviour.
     */
    @Override
    public Matrix4 getProjectionMatrix() {
        return null;
    }

    /**
     * Returns the current transform matrix. Changing this within {@link #begin()}/{@link #end()} results in undefined behaviour.
     */
    @Override
    public Matrix4 getTransformMatrix() {
        return null;
    }

    /**
     * Sets the projection matrix to be used by this Batch. If this is called inside a {@link #begin()}/{@link #end()} block, the
     * current batch is flushed to the gpu.
     *
     * @param projection
     */
    @Override
    public void setProjectionMatrix(Matrix4 projection) {

    }

    /**
     * Sets the transform matrix to be used by this Batch.
     *
     * @param transform
     */
    @Override
    public void setTransformMatrix(Matrix4 transform) {

    }

    /**
     * Sets the shader to be used in a GLES 2.0 environment. Vertex position attribute is called "a_position", the texture
     * coordinates attribute is called "a_texCoord0", the color attribute is called "a_color". See
     * {@link ShaderProgram#POSITION_ATTRIBUTE}, {@link ShaderProgram#COLOR_ATTRIBUTE} and {@link ShaderProgram#TEXCOORD_ATTRIBUTE}
     * which gets "0" appended to indicate the use of the first texture unit. The combined transform and projection matrx is
     * uploaded via a mat4 uniform called "u_projTrans". The texture sampler is passed via a uniform called "u_texture".
     * <p/>
     * Call this method with a null argument to use the default shader.
     * <p/>
     * This method will flush the batch before setting the new shader, you can call it in between {@link #begin()} and
     * {@link #end()}.
     *
     * @param shader the {@link ShaderProgram} or null to use the default shader.
     */
    @Override
    public void setShader(ShaderProgram shader) {

    }

    /**
     * @return the current {@link ShaderProgram} set by {@link #setShader(ShaderProgram)} or the defaultShader
     */
    @Override
    public ShaderProgram getShader() {
        return null;
    }

    /**
     * @return true if blending for sprites is enabled
     */
    @Override
    public boolean isBlendingEnabled() {
        return false;
    }

    /**
     * @return true if currently between begin and end.
     */
    @Override
    public boolean isDrawing() {
        return false;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {

    }
}
