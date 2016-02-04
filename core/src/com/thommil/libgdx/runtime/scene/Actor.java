package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.math.Affine2;
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
     * Components of the actor in physics/step phase
     * <br/><br/>
     * These components are modified by the physics engine and
     * should be accessed in write mode only in physics phase.
     */
    public float[] stepComponents = new float[3];

    /**
     * Components of the actor in rendering phase
     * <br/><br/>
     * These components are first set after the commit() call and can
     * be then modified in the rendering phase.
     */
    public float[] renderComponents = new float[3];


    /**
     * Commit all components from the step phase to the components of the render phase.
     * <br/><br/>
     * This method must be called before rendering when all physics components have been set.
     */
    public void commit(){
        renderComponents[X] = stepComponents[X];
        renderComponents[Y] = stepComponents[Y];
        renderComponents[ANGLE] = stepComponents[ANGLE];
    }
}
