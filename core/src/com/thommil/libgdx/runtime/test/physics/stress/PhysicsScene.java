package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.scene.listener.SceneListener;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Live benchmark on Physics/Rendering loops
 *
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game implements SceneListener{

    Scene defaultScene;
    Texture texture;

    int inc = 0;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 100;
        settings.viewport.minWorldHeight = 100;
        //settings.physics.debug = true;
        defaultScene = new Scene(settings);
        texture = new Texture(Gdx.files.internal("curiosity.png"));

        //Layer
        defaultScene.addLayer(new SpriteCacheLayer(1));
        defaultScene.addLayer(new SpriteBatchLayer(5000));

        //Actors
        defaultScene.addActor(new StaticPhysicsActor(new Texture(Gdx.files.internal("metal.png")),-100f,-50f,200f,10f));

        this.defaultScene.setSceneListener(this);

        SceneProfiler.profile(this.defaultScene, (byte)(SceneProfiler.RENDERER | SceneProfiler.PHYSICS), 5000);

        this.setScreen(defaultScene);
    }



    @Override
    public void onStep(long lastDuration) {
        if(Gdx.graphics.getFramesPerSecond() > 30) {
            if (inc % 10 == 0) {
                defaultScene.addActor(new DynamicPhysicsActor(texture));
            }
        }
        inc+=1;
    }

    @Override
    public void onRender(float deltaTime) {

    }

    @Override
    public void onResize(int width, int height) {

    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDispose() {

    }
}
