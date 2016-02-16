package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.graphics.batch.SoftBodyBatch;

/**
 * Created by tomtom on 16/02/16.
 */
public class WaterBatch extends SoftBodyBatch {

    @Override
    protected Mesh createMesh(int size) {
        return super.createMesh(size);
    }

    @Override
    protected ShaderProgram createShader() {
        return super.createShader();
    }
}
