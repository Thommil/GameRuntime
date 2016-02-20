package com.thommil.libgdx.runtime.test.render.softbody;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.graphics.batch.SoftBodyBatch;

/**
 * Created by tomtom on 20/02/16.
 */
public class ColoredSoftBodyBatch extends SoftBodyBatch {

    public ColoredSoftBodyBatch() {
        super();
    }

    public ColoredSoftBodyBatch(int size) {
        super(size);
    }

    /**
     * Create methods below must be overriden for custom Batch
     *
     * @param size
     */
    @Override
    protected Mesh createMesh(int size) {
        return super.createMesh(size);
    }

    @Override
    protected ShaderProgram createShader() {
        return super.createShader();
    }
}
