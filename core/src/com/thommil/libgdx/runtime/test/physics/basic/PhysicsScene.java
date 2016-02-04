package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.thommil.libgdx.runtime.scene.Scene;

/**
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game{

    Scene defaultScene;

    @Override
    public void create() {
        //Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 4;
        settings.viewport.minWorldHeight = 4;
        defaultScene = new Scene(settings);

        //Layer
        defaultScene.setLayer(0, new PhysicsLayer());

        //Actors
        defaultScene.addActor(new StaticPhysicsActor());
        for(int i=0;i<100;i++)
        defaultScene.addActor(new DynamicPhysicsActor());

        this.setScreen(defaultScene);

    }
}
