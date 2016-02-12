package com.thommil.libgdx.runtime.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.*;
import com.thommil.libgdx.runtime.scene.actor.SpriteActor;
import com.thommil.libgdx.runtime.scene.actor.StaticActor;

import java.nio.FloatBuffer;

/**
 * Custom Cache for simple images rendering
 *
 * Created by thommil on 12/02/16.
 */
public class BasicCache implements Disposable {
    static private final float[] tempVertices = new float[SpriteActor.VERTEX_SIZE * 6];

    private final Mesh mesh;
    private boolean drawing;
    private final Matrix4 transformMatrix = new Matrix4();
    private final Matrix4 projectionMatrix = new Matrix4();
    private Array<Cache> caches = new Array();

    private final Matrix4 combinedMatrix = new Matrix4();
    private final ShaderProgram shader;

    private Cache currentCache;
    private final Array<Texture> textures = new Array(8);
    private final IntArray counts = new IntArray(8);

    /** Number of render calls since the last {@link #begin()}. **/
    public int renderCalls = 0;

    /** Number of rendering calls, ever. Will not be reset unless set manually. **/
    public int totalRenderCalls = 0;

    private boolean blendingDisabled = false;
    private int blendSrcFunc = GL20.GL_SRC_ALPHA;
    private int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;

    /** Creates a cache that uses indexed geometry and can contain up to 1000 images. */
    public BasicCache () {
        this(1000);
    }

    /** Creates a cache with the specified size and OpenGL ES 2.0 shader.
     * @param size The maximum number of images this cache can hold. The memory required to hold the images is allocated up front.
     *           Max of 5460 if indices are used.
     */
    public BasicCache (int size) {
        if (size > 5460) throw new IllegalArgumentException("Can't have more than 5460 sprites per batch: " + size);

        mesh = new Mesh(true, size * 4, size * 6, new VertexAttribute(VertexAttributes.Usage.Position, 2,
                ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
        mesh.setAutoBind(false);

        int length = size * 6;
        short[] indices = new short[length];
        short j = 0;
        for (int i = 0; i < length; i += 6, j += 4) {
            indices[i + 0] = (short)j;
            indices[i + 1] = (short)(j + 1);
            indices[i + 2] = (short)(j + 2);
            indices[i + 3] = (short)(j + 2);
            indices[i + 4] = (short)(j + 3);
            indices[i + 5] = (short)j;
        }
        mesh.setIndices(indices);
        projectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.shader = createDefaultShader();
    }

    /** Starts the definition of a new cache, allowing the add and {@link #endCache()} methods to be called. */
    public void beginCache () {
        if (currentCache != null) throw new IllegalStateException("endCache must be called before begin.");
        currentCache = new Cache(caches.size, mesh.getVerticesBuffer().limit());
        caches.add(currentCache);
        mesh.getVerticesBuffer().compact();
    }

    /** Starts the redefinition of an existing cache, allowing the add and {@link #endCache()} methods to be called. If this is not
     * the last cache created, it cannot have more entries added to it than when it was first created. To do that, use
     * {@link #clear()} and then {@link #begin()}. */
    public void beginCache (int cacheID) {
        if (currentCache != null) throw new IllegalStateException("endCache must be called before begin.");
        if (cacheID == caches.size - 1) {
            Cache oldCache = caches.removeIndex(cacheID);
            mesh.getVerticesBuffer().limit(oldCache.offset);
            beginCache();
            return;
        }
        currentCache = caches.get(cacheID);
        mesh.getVerticesBuffer().position(currentCache.offset);
    }

    /** Ends the definition of a cache, returning the cache ID to be used with {@link #draw(int)}. */
    public int endCache () {
        if (currentCache == null) throw new IllegalStateException("beginCache must be called before endCache.");
        Cache cache = currentCache;
        int cacheCount = mesh.getVerticesBuffer().position() - cache.offset;
        if (cache.textures == null) {
            // New cache.
            cache.maxCount = cacheCount;
            cache.textureCount = textures.size;
            cache.textures = textures.toArray(Texture.class);
            cache.counts = new int[cache.textureCount];
            for (int i = 0, n = counts.size; i < n; i++)
                cache.counts[i] = counts.get(i);

            mesh.getVerticesBuffer().flip();
        } else {
            // Redefine existing cache.
            if (cacheCount > cache.maxCount) {
                throw new GdxRuntimeException(
                        "If a cache is not the last created, it cannot be redefined with more entries than when it was first created: "
                                + cacheCount + " (" + cache.maxCount + " max)");
            }

            cache.textureCount = textures.size;

            if (cache.textures.length < cache.textureCount) cache.textures = new Texture[cache.textureCount];
            for (int i = 0, n = cache.textureCount; i < n; i++)
                cache.textures[i] = textures.get(i);

            if (cache.counts.length < cache.textureCount) cache.counts = new int[cache.textureCount];
            for (int i = 0, n = cache.textureCount; i < n; i++)
                cache.counts[i] = counts.get(i);

            FloatBuffer vertices = mesh.getVerticesBuffer();
            vertices.position(0);
            Cache lastCache = caches.get(caches.size - 1);
            vertices.limit(lastCache.offset + lastCache.maxCount);
        }

        currentCache = null;
        textures.clear();
        counts.clear();

        return cache.id;
    }

    /** Invalidates all cache IDs and resets the SpriteCache so new caches can be added. */
    public void clear () {
        caches.clear();
        mesh.getVerticesBuffer().clear().flip();
    }

    /** Adds the specified vertices to the cache. Each vertex should have 5 elements, one for each of the attributes: x, y, color,
     * u, and v. If indexed geometry is used, each image should be specified as 4 vertices, otherwise each image should be
     * specified as 6 vertices. */
    public void add (Texture texture, float[] vertices, int offset, int length) {
        if (currentCache == null) throw new IllegalStateException("beginCache must be called before add.");

        int count = length / (4 * SpriteActor.VERTEX_SIZE) * 6;
        int lastIndex = textures.size - 1;
        if (lastIndex < 0 || textures.get(lastIndex) != texture) {
            textures.add(texture);
            counts.add(count);
        } else
            counts.incr(lastIndex, count);

        mesh.getVerticesBuffer().put(vertices, offset, length);
    }

    /** Adds the specified texture to the cache. */
    public void add (Texture texture, float x, float y) {
        final float fx2 = x + texture.getWidth();
        final float fy2 = y + texture.getHeight();

        tempVertices[0] = x;
        tempVertices[1] = y;
        tempVertices[2] = 0;
        tempVertices[3] = 1;

        tempVertices[4] = x;
        tempVertices[5] = fy2;
        tempVertices[6] = 0;
        tempVertices[7] = 0;

        tempVertices[8] = fx2;
        tempVertices[9] = fy2;
        tempVertices[10] = 1;
        tempVertices[11] = 0;

        tempVertices[12] = fx2;
        tempVertices[13] = y;
        tempVertices[14] = 1;
        tempVertices[15] = 1;
        add(texture, tempVertices, 0, SpriteActor.SPRITE_SIZE);

    }

    /** Adds the specified texture to the cache. */
    public void add (Texture texture, float x, float y, int srcWidth, int srcHeight, float u, float v, float u2, float v2) {
        final float fx2 = x + srcWidth;
        final float fy2 = y + srcHeight;

        tempVertices[0] = x;
        tempVertices[1] = y;
        tempVertices[2] = u;
        tempVertices[3] = v;

        tempVertices[4] = x;
        tempVertices[5] = fy2;
        tempVertices[6] = u;
        tempVertices[7] = v2;

        tempVertices[8] = fx2;
        tempVertices[9] = fy2;
        tempVertices[10] = u2;
        tempVertices[11] = v2;

        tempVertices[12] = fx2;
        tempVertices[13] = y;
        tempVertices[14] = u2;
        tempVertices[15] = v;
        add(texture, tempVertices, 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified texture to the cache. */
    public void add (Texture texture, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight) {
        final float invTexWidth = 1.0f / texture.getWidth();
        final float invTexHeight = 1.0f / texture.getHeight();
        final float u = srcX * invTexWidth;
        final float v = (srcY + srcHeight) * invTexHeight;
        final float u2 = (srcX + srcWidth) * invTexWidth;
        final float v2 = srcY * invTexHeight;
        final float fx2 = x + srcWidth;
        final float fy2 = y + srcHeight;

        tempVertices[0] = x;
        tempVertices[1] = y;
        tempVertices[2] = u;
        tempVertices[3] = v;

        tempVertices[4] = x;
        tempVertices[5] = fy2;
        tempVertices[6] = u;
        tempVertices[7] = v2;

        tempVertices[8] = fx2;
        tempVertices[9] = fy2;
        tempVertices[10] = u2;
        tempVertices[11] = v2;

        tempVertices[12] = fx2;
        tempVertices[13] = y;
        tempVertices[14] = u2;
        tempVertices[15] = v;
        add(texture, tempVertices, 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified texture to the cache. */
    public void add (Texture texture, float x, float y, float width, float height, int srcX, int srcY, int srcWidth,
                     int srcHeight, boolean flipX, boolean flipY) {

        final float invTexWidth = 1.0f / texture.getWidth();
        final float invTexHeight = 1.0f / texture.getHeight();
        float u = srcX * invTexWidth;
        float v = (srcY + srcHeight) * invTexHeight;
        float u2 = (srcX + srcWidth) * invTexWidth;
        float v2 = srcY * invTexHeight;
        final float fx2 = x + width;
        final float fy2 = y + height;

        if (flipX) {
            float tmp = u;
            u = u2;
            u2 = tmp;
        }
        if (flipY) {
            float tmp = v;
            v = v2;
            v2 = tmp;
        }

        tempVertices[0] = x;
        tempVertices[1] = y;
        tempVertices[2] = u;
        tempVertices[3] = v;

        tempVertices[4] = x;
        tempVertices[5] = fy2;
        tempVertices[6] = u;
        tempVertices[7] = v2;

        tempVertices[8] = fx2;
        tempVertices[9] = fy2;
        tempVertices[10] = u2;
        tempVertices[11] = v2;

        tempVertices[12] = fx2;
        tempVertices[13] = y;
        tempVertices[14] = u2;
        tempVertices[15] = v;
        add(texture, tempVertices, 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified texture to the cache. */
    public void add (Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
                     float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) {

        // bottom left and top right corner points relative to origin
        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        // scale
        if (scaleX != 1 || scaleY != 1) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        // construct corner points, start from top left and go counter clockwise
        final float p1x = fx;
        final float p1y = fy;
        final float p2x = fx;
        final float p2y = fy2;
        final float p3x = fx2;
        final float p3y = fy2;
        final float p4x = fx2;
        final float p4y = fy;

        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;

        // rotate
        if (rotation != 0) {
            final float cos = MathUtils.cosDeg(rotation);
            final float sin = MathUtils.sinDeg(rotation);

            x1 = cos * p1x - sin * p1y;
            y1 = sin * p1x + cos * p1y;

            x2 = cos * p2x - sin * p2y;
            y2 = sin * p2x + cos * p2y;

            x3 = cos * p3x - sin * p3y;
            y3 = sin * p3x + cos * p3y;

            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;

            x2 = p2x;
            y2 = p2y;

            x3 = p3x;
            y3 = p3y;

            x4 = p4x;
            y4 = p4y;
        }

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;

        float invTexWidth = 1.0f / texture.getWidth();
        float invTexHeight = 1.0f / texture.getHeight();
        float u = srcX * invTexWidth;
        float v = (srcY + srcHeight) * invTexHeight;
        float u2 = (srcX + srcWidth) * invTexWidth;
        float v2 = srcY * invTexHeight;

        if (flipX) {
            float tmp = u;
            u = u2;
            u2 = tmp;
        }

        if (flipY) {
            float tmp = v;
            v = v2;
            v2 = tmp;
        }

        tempVertices[0] = x1;
        tempVertices[1] = y1;
        tempVertices[2] = u;
        tempVertices[3] = v;

        tempVertices[4] = x2;
        tempVertices[5] = y2;
        tempVertices[6] = u;
        tempVertices[7] = v2;

        tempVertices[8] = x3;
        tempVertices[9] = y3;
        tempVertices[10] = u2;
        tempVertices[11] = v2;

        tempVertices[12] = x4;
        tempVertices[13] = y4;
        tempVertices[14] = u2;
        tempVertices[15] = v;
        add(texture, tempVertices, 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified region to the cache. */
    public void add (TextureRegion region, float x, float y) {
        add(region, x, y, region.getRegionWidth(), region.getRegionHeight());
    }

    /** Adds the specified region to the cache. */
    public void add (TextureRegion region, float x, float y, float width, float height) {
        final float fx2 = x + width;
        final float fy2 = y + height;
        final float u = region.getU();
        final float v = region.getV2();
        final float u2 = region.getU2();
        final float v2 = region.getV();

        tempVertices[0] = x;
        tempVertices[1] = y;
        tempVertices[2] = u;
        tempVertices[3] = v;

        tempVertices[4] = x;
        tempVertices[5] = fy2;
        tempVertices[6] = u;
        tempVertices[7] = v2;

        tempVertices[8] = fx2;
        tempVertices[9] = fy2;
        tempVertices[10] = u2;
        tempVertices[11] = v2;

        tempVertices[12] = fx2;
        tempVertices[13] = y;
        tempVertices[14] = u2;
        tempVertices[15] = v;
        add(region.getTexture(), tempVertices, 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified region to the cache. */
    public void add (TextureRegion region, float x, float y, float originX, float originY, float width, float height,
                     float scaleX, float scaleY, float rotation) {

        // bottom left and top right corner points relative to origin
        final float worldOriginX = x + originX;
        final float worldOriginY = y + originY;
        float fx = -originX;
        float fy = -originY;
        float fx2 = width - originX;
        float fy2 = height - originY;

        // scale
        if (scaleX != 1 || scaleY != 1) {
            fx *= scaleX;
            fy *= scaleY;
            fx2 *= scaleX;
            fy2 *= scaleY;
        }

        // construct corner points, start from top left and go counter clockwise
        final float p1x = fx;
        final float p1y = fy;
        final float p2x = fx;
        final float p2y = fy2;
        final float p3x = fx2;
        final float p3y = fy2;
        final float p4x = fx2;
        final float p4y = fy;

        float x1;
        float y1;
        float x2;
        float y2;
        float x3;
        float y3;
        float x4;
        float y4;

        // rotate
        if (rotation != 0) {
            final float cos = MathUtils.cosDeg(rotation);
            final float sin = MathUtils.sinDeg(rotation);

            x1 = cos * p1x - sin * p1y;
            y1 = sin * p1x + cos * p1y;

            x2 = cos * p2x - sin * p2y;
            y2 = sin * p2x + cos * p2y;

            x3 = cos * p3x - sin * p3y;
            y3 = sin * p3x + cos * p3y;

            x4 = x1 + (x3 - x2);
            y4 = y3 - (y2 - y1);
        } else {
            x1 = p1x;
            y1 = p1y;

            x2 = p2x;
            y2 = p2y;

            x3 = p3x;
            y3 = p3y;

            x4 = p4x;
            y4 = p4y;
        }

        x1 += worldOriginX;
        y1 += worldOriginY;
        x2 += worldOriginX;
        y2 += worldOriginY;
        x3 += worldOriginX;
        y3 += worldOriginY;
        x4 += worldOriginX;
        y4 += worldOriginY;

        final float u = region.getU();
        final float v = region.getV2();
        final float u2 = region.getU2();
        final float v2 = region.getV();

        tempVertices[0] = x1;
        tempVertices[1] = y1;
        tempVertices[2] = u;
        tempVertices[3] = v;

        tempVertices[4] = x2;
        tempVertices[5] = y2;
        tempVertices[6] = u;
        tempVertices[7] = v2;

        tempVertices[8] = x3;
        tempVertices[9] = y3;
        tempVertices[10] = u2;
        tempVertices[11] = v2;

        tempVertices[12] = x4;
        tempVertices[13] = y4;
        tempVertices[14] = u2;
        tempVertices[15] = v;
        add(region.getTexture(), tempVertices, 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified SpriteActor to the cache. */
    public void add (SpriteActor spriteActor) {
        add(spriteActor.texture, spriteActor.getVertices(), 0, SpriteActor.SPRITE_SIZE);
    }

    /** Adds the specified StaticActor to the cache. */
    public void add (StaticActor staticActor) {
        this.add(staticActor.texture, staticActor.x, staticActor.y,
                (int)staticActor.width, (int)staticActor.height, staticActor.u, staticActor.v, staticActor.u2, staticActor.v2);
    }

    /** Prepares the OpenGL state for SpriteCache rendering. */
    public void begin () {
        if (drawing) throw new IllegalStateException("end must be called before begin.");
        drawing = true;
        renderCalls = 0;
        combinedMatrix.set(projectionMatrix).mul(transformMatrix);

        Gdx.gl20.glDepthMask(false);

        shader.begin();
        shader.setUniformMatrix("u_projectionViewMatrix", combinedMatrix);
        shader.setUniformi("u_texture", 0);

        if (blendingDisabled) {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        } else {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            if (blendSrcFunc != -1) Gdx.gl.glBlendFunc(blendSrcFunc, blendDstFunc);
        }

        mesh.bind(shader);
    }

    /** Completes rendering for this SpriteCache. */
    public void end () {
        if (!drawing) throw new IllegalStateException("begin must be called before end.");
        drawing = false;

        shader.end();
        GL20 gl = Gdx.gl20;
        gl.glDepthMask(true);

        mesh.unbind(shader);
    }

    /** Draws all the images defined for the specified cache ID. */
    public void draw (int cacheID) {
        if (!drawing) throw new IllegalStateException("SpriteCache.begin must be called before draw.");

        final Cache cache = caches.get(cacheID);
        int offset = cache.offset / (4 * SpriteActor.VERTEX_SIZE) * 6;
        final Texture[] textures = cache.textures;
        final int[] counts = cache.counts;
        final int textureCount = cache.textureCount;
        for (int i = 0; i < textureCount; i++) {
            int count = counts[i];
            textures[i].bind();
            mesh.render(shader, GL20.GL_TRIANGLES, offset, count);
            offset += count;
        }
        renderCalls += textureCount;
        totalRenderCalls += textureCount;
    }

    /** Draws a subset of images defined for the specified cache ID.
     * @param offset The first image to render.
     * @param length The number of images from the first image (inclusive) to render. */
    public void draw (int cacheID, int offset, int length) {
        if (!drawing) throw new IllegalStateException("SpriteCache.begin must be called before draw.");

        final Cache cache = caches.get(cacheID);
        offset = offset * 6 + cache.offset;
        length *= 6;
        final Texture[] textures = cache.textures;
        final int[] counts = cache.counts;
        final int textureCount = cache.textureCount;
        for (int i = 0; i < textureCount; i++) {
            textures[i].bind();
            int count = counts[i];
            if (count > length) {
                i = textureCount;
                count = length;
            } else
                length -= count;
            mesh.render(shader, GL20.GL_TRIANGLES, offset, count);
            offset += count;
        }
        renderCalls += cache.textureCount;
        totalRenderCalls += textureCount;
    }

    /** Releases all resources held by this SpriteCache. */
    public void dispose () {
        mesh.dispose();
        if (shader != null) shader.dispose();
    }

    public Matrix4 getProjectionMatrix () {
        return projectionMatrix;
    }

    public void setProjectionMatrix (Matrix4 projection) {
        if (drawing) throw new IllegalStateException("Can't set the matrix within begin/end.");
        projectionMatrix.set(projection);
    }

    public Matrix4 getTransformMatrix () {
        return transformMatrix;
    }

    public void setTransformMatrix (Matrix4 transform) {
        if (drawing) throw new IllegalStateException("Can't set the matrix within begin/end.");
        transformMatrix.set(transform);
    }

    static private class Cache {
        final int id;
        final int offset;
        int maxCount;
        int textureCount;
        Texture[] textures;
        int[] counts;

        public Cache (int id, int offset) {
            this.id = id;
            this.offset = offset;
        }
    }

    public void disableBlending () {
        blendingDisabled = true;
    }

    public void enableBlending () {
        blendingDisabled = false;
    }

    public void setBlendFunction (int srcFunc, int dstFunc) {
        blendSrcFunc = srcFunc;
        blendDstFunc = dstFunc;
    }

    public int getBlendSrcFunc () {
        return blendSrcFunc;
    }

    public int getBlendDstFunc () {
        return blendDstFunc;
    }

    static ShaderProgram createDefaultShader () {
        String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "uniform mat4 u_projectionViewMatrix;\n" //
                + "varying vec2 v_texCoords;\n" //
                + "\n" //
                + "void main()\n" //
                + "{\n" //
                + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
                + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
                + "}\n";
        String fragmentShader = "#ifdef GL_ES\n" //
                + "precision mediump float;\n" //
                + "#endif\n" //
                + "varying vec2 v_texCoords;\n" //
                + "uniform sampler2D u_texture;\n" //
                + "void main()\n"//
                + "{\n" //
                + "  gl_FragColor = texture2D(u_texture, v_texCoords);\n" //
                + "}";
        final ShaderProgram shader = new ShaderProgram(vertexShader, fragmentShader);
        if (shader.isCompiled() == false) throw new IllegalArgumentException("Error compiling shader: " + shader.getLog());
        return shader;
    }
}
