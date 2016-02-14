package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Simple PhysicsActor test
 *
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game{

    Scene defaultScene;
    Texture texture;
    World world;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 4;
        settings.viewport.minWorldHeight = 4;
        defaultScene = new Scene(settings);
        world = defaultScene.getPhysicsWorld();
        texture = new Texture(Gdx.files.internal("curiosity.png"));

        //Layer
        defaultScene.addLayer(new SpriteBatchLayer(2));

        //Actors
        defaultScene.addActor(new StaticPhysicsActor());
        defaultScene.addActor(new DynamicPhysicsActor(texture));

        //Profiler
        SceneProfiler.profile(defaultScene);

        this.setScreen(defaultScene);

    }

}
