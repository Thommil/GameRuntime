package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.physics.LogicActor;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Live benchmark on Physics/Rendering loops
 *
 * Created by tomtom on 04/02/16.
 */
public class PhysicsStressTestScene extends Game{

    Scene scene;
    Texture curiosityTexture;

    int inc = 0;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 100;
        settings.viewport.minWorldHeight = 100;
        //settings.physics.debug = true;
        scene = new Scene(settings);
        curiosityTexture = new Texture(Gdx.files.internal("curiosity.png"));

        //Layer
        scene.addLayer(new SpriteCacheLayer(1));
        scene.addLayer(new SpriteBatchLayer(5000));

        //Actors
        scene.addActor(new GroundActor(new Texture(Gdx.files.internal("metal.png")),-100f,-50f,200f,10f));

        scene.addActor(new LogicActor() {
            @Override
            public void step(long lastStepDuration) {
                if(Gdx.graphics.getFramesPerSecond() > 30) {
                    if (inc % 10 == 0) {
                        scene.addActor(new CuriosityActor(curiosityTexture));
                    }
                }
                inc+=1;
            }
        });

        SceneProfiler.profile(this.scene, (byte)(SceneProfiler.RENDERER | SceneProfiler.PHYSICS), 5000);

        this.setScreen(scene);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.curiosityTexture.dispose();
    }
}
