package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.thommil.libgdx.runtime.graphics.renderer.TextureSet;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Simple PhysicsActor test
 *
 * Created by tomtom on 04/02/16.
 */
public class BasicPhysicsTestScene extends Game{

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 4;
        settings.viewport.minWorldHeight = 4;
        Scene scene = new Scene(settings);

        //Layer
        scene.addLayer(new SpriteBatchLayer(2));

        //Actors
        scene.addActor(new GroundActor(new TextureSet(new Texture(Gdx.files.internal("metal.png"))),-2f,-2f,4f,1f));
        scene.addActor(new CuriosityActor(new TextureSet(new Texture(Gdx.files.internal("curiosity.png")))));

        //Profiler
        SceneProfiler.profile(scene, SceneProfiler.ALL, 5000);

        this.setScreen(scene);

    }

}
