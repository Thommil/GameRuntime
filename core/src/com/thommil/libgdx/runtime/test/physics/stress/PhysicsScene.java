package com.thommil.libgdx.runtime.test.physics.stress;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.SceneListener;

/**
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
        //settings.physics.asyncMode=false;
        settings.physics.velocityIterations = 8;
        settings.physics.frequency = 1/60f;
        settings.physics.positionIterations = 3;
        defaultScene = new Scene(settings);
        world = defaultScene.getPhysicsWorld();
        texture = new Texture(Gdx.files.internal("curiosity.png"));

        //Layer
        defaultScene.setLayer(0, new PhysicsLayer());

        //Actors
        defaultScene.addActor(new StaticPhysicsActor());

        this.defaultScene.setListener(this);

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
        if(inc%60 == 0) {
            Gdx.app.log("", "FPS : " + Gdx.graphics.getFramesPerSecond() + " " + this.defaultScene.getPhysicsWorld().getBodyCount());
        }
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
        PhysicsScene.this.defaultScene.removeActor(PhysicsScene.this.defaultScene.getPhysicsActors().get(1));
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
