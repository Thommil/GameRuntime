package com.thommil.libgdx.runtime.graphics.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.graphics.TextureSet;

/**
 * Created by thommil on 3/1/16.
 */
public interface CacheRenderer extends Batch{

    void setCombinedMatrix(final Matrix4 combinedMatrix);

    void beginCache ();

    void beginCache(int cacheId);

    void add (TextureSet textureSet, float[] vertices, int offset, int length);

    void add (TextureSet textureSet, float x, float y, float srcWidth, float srcHeight, float u, float v, float u2, float v2, float color);

    int endCache ();

    void draw(int cacheId);

    void clear();
}
