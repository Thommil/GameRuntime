package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.utils.Disposable;

/**
 * Definition of a Renderer
 *
 * Created by thommil on 2/23/16.
 */
public interface Renderer extends Disposable{

    /**
     * Called at beginning of rendering
     */
    void begin();

    /**
     * Generic method to draw a set of vertices.
     *
     * @param vertices The list of vertices to draw
     */
    void draw(float[] vertices);

    /**
     * Generic method to draw a predefined object
     *
     * @param id The ID of the object
     */
    void draw(int id);

    /**
     * Called at ending of rendering
     */
    void end();


}
