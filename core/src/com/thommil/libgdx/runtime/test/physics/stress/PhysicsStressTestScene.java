package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.thommil.libgdx.runtime.graphics.renderer.TextureSet;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.physics.AbstractStepable;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Live benchmark on Physics/Rendering loops
 *
 * Created by tomtom on 04/02/16.
 */
public class PhysicsStressTestScene extends Game{

    Scene scene;
    TextureSet curiosityTextureSet;

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
        curiosityTextureSet = new TextureSet(new Texture(Gdx.files.internal("curiosity.png")));

        //Layer
        scene.addLayer(new SpriteBatchLayer(5000));

        //Actors
        scene.addActor(new GroundActor(new TextureSet(new Texture(Gdx.files.internal("metal.png"))),-100f,-50f,200f,10f));

        scene.addActor(new AbstractStepable(MathUtils.random(0x7ffffffe)) {

            @Override
            public void step(long lastStepDuration) {
                if(Gdx.graphics.getFramesPerSecond() > 30) {
                    if (inc % 30 == 0) {
                        scene.addActor(new CuriosityActor(curiosityTextureSet));
                    }
                }
                inc+=1;
            }

            @Override public void dispose() {}
        });

        SceneProfiler.profile(scene, SceneProfiler.ALL, 5000);

        this.setScreen(scene);
    }

    @Override
    public void dispose() {
        super.dispose();
        this.curiosityTextureSet.dispose();
    }
}
