package com.thommil.libgdx.runtime.test.physics;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.thommil.libgdx.runtime.scene.Scene;

/**
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game {

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 4;
        settings.viewport.minWorldHeight = 4;
        settings.renderer.layers = 1;

        //Layer

        //Actors
    }
}
