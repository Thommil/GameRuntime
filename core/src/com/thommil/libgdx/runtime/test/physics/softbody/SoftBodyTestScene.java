package com.thommil.libgdx.runtime.test.physics.softbody;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SoftBodyBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;
import finnstr.libgdx.liquidfun.ParticleDef;
import finnstr.libgdx.liquidfun.ParticleGroupDef;

/**
 * Live benchmark on Physics/Rendering loops
 *
 * Created by tomtom on 04/02/16.
 */
public class SoftBodyTestScene extends Game implements InputProcessor{

    Scene scene;
    SoftbodyActor particlesActor;

    int inc = 0;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 10;
        settings.viewport.minWorldHeight = 10;
        settings.physics.particleIterations = 6;
        //settings.physics.debug = true;
        scene = new Scene(settings);

        //Particles
        scene.addLayer(new SoftBodyBatchLayer(10000));
        particlesActor = new SoftbodyActor();
        scene.addActor(particlesActor);

        //Container
        SpriteCacheLayer.setMaxSprites(3);
        SpriteCacheLayer containerLayer = new SpriteCacheLayer();
        scene.addLayer(containerLayer);

        Texture texture = new Texture(Gdx.files.internal("metal.png"));
        texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        containerLayer.beginCache();
        scene.addActor(new ContainerActor(texture,-5f,-5f,10f,1f));
        scene.addActor(new ContainerActor(texture,-5f,-5f,0.5f,8f));
        scene.addActor(new ContainerActor(texture,4.5f,-5f,0.5f,8f));
        containerLayer.endCache();

        Gdx.input.setInputProcessor(this);

        SceneProfiler.profile(this.scene, (byte)(SceneProfiler.RENDERER | SceneProfiler.PHYSICS), 5000);

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
        this.scene.runOnPhysicsThread(new Runnable() {
            @Override
            public void run() {
                ParticleGroupDef particleGroupDef = new ParticleGroupDef();
                particleGroupDef.flags.add(ParticleDef.ParticleType.b2_elasticParticle);
                particleGroupDef.position.set(0f,5f);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.2f,2f);
                particleGroupDef.shape = shape;
                SoftBodyTestScene.this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
                shape.dispose();
            }
        });

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
