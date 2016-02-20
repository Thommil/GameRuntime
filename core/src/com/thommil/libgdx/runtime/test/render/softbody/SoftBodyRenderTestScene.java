package com.thommil.libgdx.runtime.test.render.softbody;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.SoftBodyBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;
import finnstr.libgdx.liquidfun.ParticleDef;
import finnstr.libgdx.liquidfun.ParticleGroupDef;

/**
 * SoftBody textured display
 *
 * Created by tomtom on 04/02/16.
 */
public class SoftBodyRenderTestScene extends Game implements InputProcessor{

    Scene scene;
    SoftbodyRenderActor particlesActor;

    int inc = 0;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 10;
        settings.viewport.minWorldHeight = 10;
        settings.physics.particleIterations = 8;
        //settings.renderer.blendEnabled=false;
        //settings.physics.debug = true;
        scene = new Scene(settings);

        //Particles
        SoftBodyBatchLayer softBodyBatchLayer = new SoftBodyBatchLayer(30000);
        softBodyBatchLayer.setScaleFactor(1.60f);
        scene.addLayer(1,softBodyBatchLayer);
        particlesActor = new SoftbodyRenderActor();
        scene.addActor(particlesActor);

        //Container
        SpriteCacheLayer.setMaxSprites(3);
        SpriteCacheLayer containerLayer = new SpriteCacheLayer();
        scene.addLayer(0,containerLayer);

        Texture texture = new Texture(Gdx.files.internal("metal.png"));
        texture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        containerLayer.beginCache();
        scene.addActor(new ContainerActor(texture,-5f,-5f,10f,1f));
        scene.addActor(new ContainerActor(texture,-5f,-5f,0.5f,8f));
        scene.addActor(new ContainerActor(texture,4.5f,-5f,0.5f,8f));
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
        this.scene.runOnPhysicsThread(new Runnable() {
            @Override
            public void run() {
                ParticleGroupDef particleGroupDef = new ParticleGroupDef();
                particleGroupDef.flags.add(ParticleDef.ParticleType.b2_elasticParticle);
                particleGroupDef.position.set(0f,0f);

                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.3f,2f);
                particleGroupDef.shape = shape;
                SoftBodyRenderTestScene.this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
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
