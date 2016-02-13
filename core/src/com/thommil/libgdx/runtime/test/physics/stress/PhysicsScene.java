package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.BasicBatchLayer;
import com.thommil.libgdx.runtime.scene.listener.SceneListener;
import finnstr.libgdx.liquidfun.ParticleBodyContact;
import finnstr.libgdx.liquidfun.ParticleContact;
import finnstr.libgdx.liquidfun.ParticleSystem;

/**
 * Live benchmark on Physics/Rendering loops
 *
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game implements SceneListener, InputProcessor{

    Scene defaultScene;
    Texture texture;
    World world;
    final FPSLogger fpsLogger = new FPSLogger();
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
        defaultScene.addLayer(0, new BasicBatchLayer(1));
        defaultScene.addLayer(1, new BasicBatchLayer(5000));

        //Actors
        defaultScene.addActor(new StaticPhysicsActor());

        this.defaultScene.setListener(this);

        Gdx.input.setInputProcessor(this);

        this.setScreen(defaultScene);

    }



    @Override
    public void onStep(long lastDuration) {
        if(Gdx.graphics.getFramesPerSecond() > 30) {
            if (inc % 30 == 0) {
                defaultScene.addActor(new DynamicPhysicsActor(texture));
            }
        }
        inc+=1;
    }

    @Override
    public void onRender(float deltaTime) {
        fpsLogger.log();
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void beginParticleBodyContact(ParticleSystem particleSystem, ParticleBodyContact particleBodyContact) {

    }

    @Override
    public void endParticleBodyContact(Fixture fixture, ParticleSystem particleSystem, int i) {

    }

    @Override
    public void beginParticleContact(ParticleSystem particleSystem, ParticleContact particleContact) {

    }

    @Override
    public void endParticleContact(ParticleSystem particleSystem, int i, int i1) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

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
