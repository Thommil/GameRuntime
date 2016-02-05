package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.SceneListener;

/**
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game implements SceneListener{

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
        defaultScene.addActor(new DynamicPhysicsActor());

        this.defaultScene.setListener(this);

        this.setScreen(defaultScene);

    }

    @Override
    public void onStep(long lastDuration) {

    }

    @Override
    public void onRender(float deltaTime) {

    }
}
