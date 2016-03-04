package com.thommil.libgdx.runtime.graphics.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.graphics.TextureSet;

/**
 * BatchRenderer interface to merge runtime renderers with LibGDX Batch
 *
 * @author Thommil on 3/1/16.
 */
public interface BatchRenderer extends Batch {

    /**
     * Set the MVP matrix of the renderer
     */
    void setCombinedMatrix(final Matrix4 combinedMatrix);

    /**
     * Get the MVP matrix of the renderer
     */
    Matrix4 getCombinedMatrix();

    /**
     * Draw generic method with textureSet support
     *
     * @param textureSet The textureset to draw
     * @param vertices The vertices to add to the Batch
     * @param offset The offset in vertices array
     * @param count The number of vertices to add
     */
    void draw(TextureSet textureSet, float[] vertices, int offset, int count);
}
