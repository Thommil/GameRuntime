package com.thommil.libgdx.runtime.test.physics.basic;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.SceneListener;

/**
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game implements SceneListener, InputProcessor{

    Scene defaultScene;
    Texture texture;
    World world;

    @Override
    public void create() {
        //Gdx.app.setLogLevel(Application.LOG_DEBUG);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 4;
        settings.viewport.minWorldHeight = 4;
        //settings.physics.asyncMode=false;
        defaultScene = new Scene(settings);
        world = defaultScene.getPhysicsWorld();
        texture = new Texture(Gdx.files.internal("curiosity.png"));

        //Layer
        defaultScene.setLayer(0, new PhysicsLayer());

        //Actors
        defaultScene.addActor(new StaticPhysicsActor());
        defaultScene.addActor(new DynamicPhysicsActor(texture));

        this.defaultScene.setListener(this);

        Gdx.input.setInputProcessor(this);

        this.setScreen(defaultScene);

    }

    @Override
    public void onStep(long lastDuration) {
        //this.defaultScene.addActor(new DynamicPhysicsActor(texture));
        //Gdx.app.log("","FPS : " + Gdx.graphics.getFramesPerSecond()+ " " + this.defaultScene.getPhysicsWorld().getBodyCount());
    }

    @Override
    public void onRender(float deltaTime) {

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
        //this.defaultScene.addActor(new DynamicPhysicsActor(texture));
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
