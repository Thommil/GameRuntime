package com.thommil.libgdx.runtime.test.render.softbody;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SoftBodyBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;

/**
 * SoftBody textured display
 *
 * This demo is purely based on physics and is heavy in CPU. To be included in a game
 * the amount of particles must be decreased to allow less iterations and blur pass using FBO
 * can be used like in the WaterScene demo.
 *
 * The colors of the particles are not used as they are always the same and to avoid big buffer on draw.
 *
 * Created by tomtom on 04/02/16.
 */
public class SoftBodyRenderTestScene extends Game implements InputProcessor{

    Scene scene;
    ColoredSoftbodyActor coloredSoftBodyActor;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 10;
        settings.viewport.minWorldHeight = 10;
        settings.physics.particleIterations = 6;
        //settings.renderer.blendEnabled=false;
        //settings.physics.debug = true;
        scene = new Scene(settings);

        //Particles
        SoftBodyBatchLayer softBodyBatchLayer = new ColoredSoftBodyLayer(1089);
        softBodyBatchLayer.setScaleFactor(1.2f);
        //softBodyBatchLayer.setScaleFactor(0.75f);
        scene.addLayer(1,softBodyBatchLayer);
        coloredSoftBodyActor = new ColoredSoftbodyActor();
        scene.addActor(coloredSoftBodyActor);

        //Container
        SpriteCacheLayer.setSize(4);
        SpriteCacheLayer containerLayer = new SpriteCacheLayer(4);
        scene.addLayer(0,containerLayer);

        Texture texture = new Texture(Gdx.files.internal("metal.png"));
        texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        containerLayer.beginCache();
        scene.addActor(new ContainerActor(texture,-5f,-5f,10f,0.5f));
        scene.addActor(new ContainerActor(texture,-5f,4.5f,10f,0.5f));
        scene.addActor(new ContainerActor(texture,-5f,-5f,0.5f,10f));
        scene.addActor(new ContainerActor(texture,4.5f,-5f,0.5f,10f));
        containerLayer.endCache();

        Gdx.input.setInputProcessor(this);

        SceneProfiler.profile(scene, SceneProfiler.ALL, 5000);

        this.setScreen(scene);

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
        coloredSoftBodyActor.innerSoftBody.applyLinearImpulse(new Vector2(MathUtils.random(-100,100),100));

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
