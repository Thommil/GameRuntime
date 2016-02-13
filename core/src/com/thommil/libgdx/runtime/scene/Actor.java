package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.utils.Disposable;

/**
 * Base element of a Scene, represents any entity contained in Scene.
 *
 * Created by thommil on 01/02/16.
 */
public interface Actor extends Disposable{

    /**
     *  Gets the ID of the Actor
     */
    int getId();

}
