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
import com.thommil.libgdx.runtime.scene.listener.SceneListener;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * Live benchmark on Physics/Rendering loops
 *
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game implements SceneListener, InputProcessor{

    Scene defaultScene;
    Texture texture;
    World world;
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
        world = defaultScene.getPhysicsWorld();
        texture = new Texture(Gdx.files.internal("curiosity.png"));

        //Layer
        defaultScene.addLayer(new SpriteBatchLayer(1));
        defaultScene.addLayer(new SpriteBatchLayer(5000));

        //Actors
        defaultScene.addActor(new StaticPhysicsActor());

        this.defaultScene.setSceneListener(this);

        SceneProfiler.profile(this.defaultScene, (byte)(SceneProfiler.RENDERER | SceneProfiler.PHYSICS), 5000);

        Gdx.input.setInputProcessor(this);

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
    public void onResize() {

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

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.defaultScene.removeActor((Actor) PhysicsScene.this.defaultScene.getCollidables().get(0));
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
