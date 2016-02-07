package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.Scene;

/**
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game{

    Scene defaultScene;
    Texture texture;
    World world;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_ERROR);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 4;
        settings.viewport.minWorldHeight = 4;
        defaultScene = new Scene(settings);
        world = defaultScene.getPhysicsWorld();
        texture = new Texture(Gdx.files.internal("curiosity.png"));

        //Layer
        defaultScene.addLayer(0, new PhysicsLayer());

        //Actors
        defaultScene.addActor(new StaticPhysicsActor());
        defaultScene.addActor(new DynamicPhysicsActor(texture));

        this.setScreen(defaultScene);

    }

}
