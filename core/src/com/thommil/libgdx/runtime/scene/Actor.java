package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.utils.Disposable;

/**
 * Base element of a Scene, represents any entity contained in Scene.
 *
 * Created by thommil on 01/02/16.
 */
public abstract class Actor implements Disposable{

    /**
     * The X coordinates index in components of the actor
     */
    public static final int X = 0;

    /**
     * The X coordinates index in components of the actor
     */
    public static final int Y = 1;

    /**
     * The angle in rads of the rotation component of the actor
     */
    public static final int ANGLE = 2;

    /**
     * Components of the actor in rendering phase
     * <br/><br/>
     * These components are first set after the commit() call and can
     * be then modified in the rendering phase.
     */
    public float[] components = new float[3];
}
