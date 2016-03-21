package com.thommil.libgdx.runtime.graphics.renderer.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.*;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.actor.graphics.SpriteActor;
import com.thommil.libgdx.runtime.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.graphics.renderer.CacheRenderer;

import java.nio.FloatBuffer;

/**
 * Custom LibGDX CacheRenderer for simple images rendering
 *
 * @author thommil on 03/02/16.
 */
public class SpriteCacheRenderer implements CacheRenderer {

    protected final Mesh mesh;
    protected final ShaderProgram shader;

    protected final float[] tempVertices = new float[SpriteActor.VERTEX_SIZE * 6];
    protected final Array<Cache> caches = new Array<Cache>();
    protected final Array<TextureSet> textureSets = new Array<TextureSet>(8);
    protected final IntArray counts = new IntArray(8);
    protected Cache currentCache;

    protected final Matrix4 combinedMatrix = new Matrix4();

    /** Creates a cache with the specified size and OpenGL ES 2.0 shader.
     * @param size The maximum number of images this cache can hold. The memory required to hold the images is allocated up front.
     *           Max of 5460 if indices are used.
     */
    public SpriteCacheRenderer(final int size) {
        this.mesh = createMesh(size);
        this.shader = createShader();
    }

    /** Starts the definition of a new cache, allowing the add and {@link #endCache()} methods to be called. */
    @Override
    public void beginCache () {
        this.currentCache = new Cache(this.caches.size, this.mesh.getVerticesBuffer().limit());
        this.caches.add(this.currentCache);
        this.mesh.getVerticesBuffer().compact();
    }

    /** Starts the redefinition of an existing cache, allowing the add and {@link #endCache()} methods to be called. If this is not
     * the last cache created, it cannot have more entries added to it than when it was first created. To do that, use*/
    @Override
    public void beginCache (int cacheID) {
        if (cacheID == this.caches.size - 1) {
            Cache oldCache = this.caches.removeIndex(cacheID);
            this.mesh.getVerticesBuffer().limit(oldCache.offset);
            beginCache();
            return;
        }
        this.currentCache = this.caches.get(cacheID);
        this.mesh.getVerticesBuffer().position(this.currentCache.offset);
    }

    /** Ends the definition of a cache, returning the cache ID to be used with {@link #draw(int)}. */
    @Override
    public int endCache () {
        final Cache cache = this.currentCache;
        int cacheCount = this.mesh.getVerticesBuffer().position() - cache.offset;
        if (cache.textureSets == null) {
            // New cache.
            cache.maxCount = cacheCount;
            cache.textureCount = this.textureSets.size;
            cache.textureSets = this.textureSets.toArray(TextureSet.class);
            cache.counts = new int[cache.textureCount];
            for (int i = 0, n = this.counts.size; i < n; i++)
                cache.counts[i] = this.counts.get(i);

            this.mesh.getVerticesBuffer().flip();
        } else {
            // Redefine existing cache.
            if (cacheCount > cache.maxCount) {
                throw new GdxRuntimeException(
                        "If a cache is not the last created, it cannot be redefined with more entries than when it was first created: "
                                + cacheCount + " (" + cache.maxCount + " max)");
            }

            cache.textureCount = this.textureSets.size;

            if (cache.textureSets.length < cache.textureCount) cache.textureSets = new TextureSet[cache.textureCount];
            for (int i = 0, n = cache.textureCount; i < n; i++)
                cache.textureSets[i] = this.textureSets.get(i);

            if (cache.counts.length < cache.textureCount) cache.counts = new int[cache.textureCount];
            for (int i = 0, n = cache.textureCount; i < n; i++)
                cache.counts[i] = this.counts.get(i);

            FloatBuffer vertices =this. mesh.getVerticesBuffer();
            vertices.position(0);
            Cache lastCache = this.caches.get(caches.size - 1);
            vertices.limit(lastCache.offset + lastCache.maxCount);
        }

        this.currentCache = null;
        this.textureSets.clear();
        this.counts.clear();

        return cache.id;
    }

    /** Invalidates all cache IDs and resets the CacheRenderer so new caches can be added. */
    @Override
    public void clear () {
        this.caches.clear();
        this.mesh.getVerticesBuffer().clear().flip();
    }

    /** Adds the specified vertices to the cache. Each vertex should have 5 elements, one for each of the attributes: x, y, color,
     * u, and v. If indexed geometry is used, each image should be specified as 4 vertices, otherwise each image should be
     * specified as 6 vertices. */
    @Override
    public void add (TextureSet textureSet, float[] vertices, int offset, int length) {
        final int count = length / (4 * SpriteActor.VERTEX_SIZE) * 6;
        final int lastIndex = this.textureSets.size - 1;
        if (lastIndex < 0 || this.textureSets.get(lastIndex) != textureSet) {
            this.textureSets.add(textureSet);
            this.counts.add(count);
        } else
            this.counts.incr(lastIndex, count);

        this.mesh.getVerticesBuffer().put(vertices, offset, length);
    }

    /** Adds the specified texture to the cache. */
    @Override
    public void add (TextureSet textureSet, float x, float y, float srcWidth, float srcHeight, float u, float v, float u2, float v2, float color) {
        final float fx2 = x + srcWidth;
        final float fy2 = y + srcHeight;

        this.tempVertices[0] = x;
        this.tempVertices[1] = y;
        this.tempVertices[2] = color;
        this.tempVertices[3] = u;
        this.tempVertices[4] = v;

        this.tempVertices[5] = x;
        this.tempVertices[6] = fy2;
        this.tempVertices[7] = color;
        this.tempVertices[8] = u;
        this.tempVertices[9] = v2;

        this.tempVertices[10] = fx2;
        this.tempVertices[11] = fy2;
        this.tempVertices[12] = color;
        this.tempVertices[13] = u2;
        this.tempVertices[14] = v2;

        this.tempVertices[15] = fx2;
        this.tempVertices[16] = y;
        this.tempVertices[17] = color;
        this.tempVertices[18] = u2;
        this.tempVertices[19] = v;
        add(textureSet, this.tempVertices, 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified SpriteActor to the cache. */
    public void add (SpriteActor spriteActor) {
        this.add(spriteActor.textureSet, spriteActor.getVertices(), 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified StaticActor to the cache. */
    public void add (StaticActor staticActor) {
        this.add(staticActor.textureSet, staticActor.x, staticActor.y,
                staticActor.width, staticActor.height, staticActor.u, staticActor.v, staticActor.u2, staticActor.v2, staticActor.color);
    }

    @Override
    public void setCombinedMatrix(final Matrix4 combinedMatrix) {
        this.combinedMatrix.set(combinedMatrix);
    }

    @Override
    public Matrix4 getCombinedMatrix() {
        return this.combinedMatrix;
    }

    /** Prepares the OpenGL state for CacheRenderer rendering. */
    @Override
    public void begin () {
        this.shader.begin();
        this.shader.setUniformMatrix("u_projectionViewMatrix", this.combinedMatrix);
        this.mesh.bind(shader);
    }

    /** Completes rendering for this CacheRenderer. */
    @Override
    public void end () {
        this.shader.end();
        this.mesh.unbind(this.shader);
    }

    /** Draws all the images defined for the specified cache ID. */
    @Override
    public void draw (int cacheID) {
        final Cache cache = this.caches.get(cacheID);
        int offset = cache.offset / (4 * SpriteActor.VERTEX_SIZE) * 6;
        final TextureSet[] textureSets = cache.textureSets;
        final int[] counts = cache.counts;
        final int textureCount = cache.textureCount;
        int lastTextureSetSize = 0;
        for (int i = 0; i < textureCount; i++) {
            int count = counts[i];
            if(textureSets[i].textures.length != lastTextureSetSize){
                textureSets[i].setUniformAll(this.shader);
                lastTextureSetSize = textureSets[i].textures.length;
            }
            textureSets[i].bindAll();
            this.mesh.render(shader, GL20.GL_TRIANGLES, offset, count);
            offset += count;
        }
    }

    @Override
    public void dispose () {
        this.mesh.dispose();
        if (this.shader != null) this.shader.dispose();
    }

    static private class Cache {
        final int id;
        final int offset;
        int maxCount;
        int textureCount;
        TextureSet[] textureSets;
        int[] counts;

        public Cache (int id, int offset) {
            this.id = id;
            this.offset = offset;
        }
    }

    /**
     * Subclasses should override this method to use their specific Mesh
     */
    protected Mesh createMesh(final int size){
        if (size > 5460) throw new IllegalArgumentException("Can't have more than 5460 sprites per batch: " + size);

        Mesh.VertexDataType vertexDataType = Mesh.VertexDataType.VertexBufferObject;
        if (Gdx.gl30 != null) {
            vertexDataType = Mesh.VertexDataType.VertexBufferObjectWithVAO;
        }
        final Mesh mesh = new Mesh(vertexDataType, false, size * 4, size * 6, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE),
                new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
        mesh.setAutoBind(false);

        int length = size * 6;
        short[] indices = new short[length];
        short j = 0;
        for (int i = 0; i < length; i += 6, j += 4) {
            indices[i] = j;
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = j;
        }
        mesh.setIndices(indices);

        return mesh;
    }

    /**
     * Subclasses should override this method to use their specific vertices
     */
    protected ShaderProgram createShader () {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "uniform mat4 u_projectionViewMatrix;\n" //
                + "varying vec4 v_color;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "precision mediump float;\n" //
                + "#endif\n" //
                + "varying vec2 v_texCoords;\n" //
                + "varying vec4 v_color;\n" //
                + "uniform sampler2D "+TextureSet.UNIFORM_TEXTURE_0+";\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = v_color * texture2D("+TextureSet.UNIFORM_TEXTURE_0+", v_texCoords);\n" //
                + "}";
        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }

    @Override
    public ShaderProgram getShader() {
        return this.shader;
    }

    /**
     * NOT IMPLEMENTED API
     */
    @Override public void setColor(Color tint) {}
    @Override public void setColor(float r, float g, float b, float a) {}
    @Override public Color getColor() {return null;}
    @Override public void setColor(float color) {}
    @Override public void draw(Texture texture, float[] spriteVertices, int offset, int count) {}
    @Override public void flush() {}
    @Override public float getPackedColor() {return 0;}
    @Override public void draw(Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {}
    @Override public void draw(Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {}
    @Override public void draw(Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {}
    @Override public void draw(Texture texture, float x, float y, float width, float height, float u, float v, float u2, float v2) {}
    @Override public void draw(Texture texture, float x, float y) {}
    @Override public void draw(Texture texture, float x, float y, float width, float height) {}
    @Override public void draw(TextureRegion region, float x, float y) {}
    @Override public void draw(TextureRegion region, float x, float y, float width, float height) {}
    @Override public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation) {}
    @Override public void draw(TextureRegion region, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, boolean clockwise) {}
    @Override public void draw(TextureRegion region, float width, float height, Affine2 transform) {}
    @Override public void disableBlending() {}
    @Override public void enableBlending() {}
    @Override public void setBlendFunction(int srcFunc, int dstFunc) {}
    @Override public int getBlendSrcFunc() {return 0;}
    @Override public int getBlendDstFunc() {return 0;}
    @Override public Matrix4 getProjectionMatrix() {return null;}
    @Override public Matrix4 getTransformMatrix() {return null;}
    @Override public void setProjectionMatrix(Matrix4 projection) {}
    @Override public void setTransformMatrix(Matrix4 transform) {}
    @Override public void setShader(ShaderProgram shader) {}
    @Override public boolean isBlendingEnabled() {return false;}
    @Override public boolean isDrawing() {return false;}
}
