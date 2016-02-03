package com.thommil.libgdx.runtime.actor;

/**
 * Base element of a Scene, represents any entity contained in Scene.
 *
 * Created by thommil on 01/02/16.
 */
public abstract class Actor {

    /**
     * Returns the layer of the actor
     * <br/><br/>
     * Try to organize your layer based on rendering routines (SpriteBatch)
     *
     * @return The layer of the Renderable
     */
    public abstract int getLayer();
}
