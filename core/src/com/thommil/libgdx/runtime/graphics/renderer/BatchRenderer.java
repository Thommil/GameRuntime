package com.thommil.libgdx.runtime.graphics.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.thommil.libgdx.runtime.graphics.TextureSet;

/**
 * Created by thommil on 3/1/16.
 */
public interface BatchRenderer extends Batch {

    void setCombinedMatrix(final Matrix4 combinedMatrix);

    void draw(TextureSet textureSet, float[] vertices, int offset, int count);
}
