package com.thommil.libgdx.runtime.graphics.renderer.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.thommil.libgdx.runtime.graphics.renderer.Renderer;

/**
 * Adapter to allow use of SpriteBatch in layers by actors
 *
 * @author thommil on 03/02/16.
 */
public class SpriteBatchAdapter extends SpriteBatch implements Renderer{

    /**
     * Constructs a new SpriteBatch with a size of 1000, one buffer, and the default shader.
     *
     * @see SpriteBatch#SpriteBatch(int, ShaderProgram)
     */
    public SpriteBatchAdapter() {
        super();
    }

    /**
     * Constructs a SpriteBatch with one buffer and the default shader.
     *
     * @param size
     * @see SpriteBatch#SpriteBatch(int, ShaderProgram)
     */
    public SpriteBatchAdapter(int size) {
        super(size);
    }

    /**
     * Constructs a new SpriteBatch. Sets the projection matrix to an orthographic projection with y-axis point upwards, x-axis
     * point to the right and the origin being in the bottom left corner of the screen. The projection will be pixel perfect with
     * respect to the current screen resolution.
     * <p/>
     * The defaultShader specifies the shader to use. Note that the names for uniforms for this default shader are different than
     * the ones expect for shaders set with {@link #setShader(ShaderProgram)}. See {@link #createDefaultShader()}.
     *
     * @param size          The max number of sprites in a single batch. Max of 5460.
     * @param defaultShader The default shader to use. This is not owned by the SpriteBatch and must be disposed separately.
     */
    public SpriteBatchAdapter(int size, ShaderProgram defaultShader) {
        super(size, defaultShader);
    }

    /**
     * Generic method to draw a set of vertices.
     *
     * @param vertices The list of vertices to draw
     */
    @Override
    public void draw(float[] vertices) {
        //Not implemented
    }

    /**
     * Generic method to draw a predefined object
     *
     * @param id The ID of the object
     */
    @Override
    public void draw(int id) {
        //Not implemented
    }
}
