package com.thommil.libgdx.runtime.graphics.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.graphics.TextureSet;

/**
 * CacheRenderer interface to merge runtime renderers with LibGDX Cache and Batch
 *
 * @author Thommil on 3/1/16.
 */
public interface CacheRenderer extends Batch{

    /**
     * Set the MVP matrix of the renderer
     */
    void setCombinedMatrix(final Matrix4 combinedMatrix);

    /**
     * Get the MVP matrix of the renderer
     */
    Matrix4 getCombinedMatrix();

    /**
     * Starts a new cache
     */
    void beginCache ();

    /**
     * Starts an existing cache (clear the previous)
     *
     * @param cacheId The cache ID to use
     */
    void beginCache(int cacheId);

    /**
     * Adds elements to the cache
     *
     * @param textureSet The texture set to use
     * @param vertices The vertices to add to the mesh
     * @param offset The offset in the vertices array
     * @param length The number of vertices to add
     */
    void add (TextureSet textureSet, float[] vertices, int offset, int length);

    /**
     * Adds elements to the cache to be drawn with specific parameters
     *
     * @param textureSet The texture set to use
     * @param vertices The vertices to add to the mesh
     * @param offset The offset in the vertices array
     * @param length The number of vertices to add
     */
    void add (TextureSet textureSet, float x, float y, float srcWidth, float srcHeight, float u, float v, float u2, float v2, float color);

    /**
     * Ends current cache creation
     *
     * @return The cache ID
     */
    int endCache ();

    /**
     * Draw the cache ID
     *
     * @param cacheId The cache ID
     */
    void draw(int cacheId);

    /**
     * Empty all entries of the cache
     */
    void clear();
}
