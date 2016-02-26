package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.utils.Disposable;

/**
 * Base element of a Scene, represents any entity contained in Scene.
 *
 * @author thommil on 03/02/16.
 */
public abstract class Actor implements Disposable{

    protected final int id;

    /**
     * Default constructor
     *
     * @param id The ID of the actor
     */
    public Actor(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the Actor
     */
    public int getId() {
        return this.id;
    }

}
