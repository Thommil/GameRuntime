package com.thommil.libgdx.runtime.test.physics.softbody;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.thommil.libgdx.runtime.graphics.batch.ParticlesBatch;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.layer.ParticlesBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;
import finnstr.libgdx.liquidfun.ParticleDef;
import finnstr.libgdx.liquidfun.ParticleGroup;
import finnstr.libgdx.liquidfun.ParticleGroupDef;

/**
 * Softbody raw display
 *
 * Created by tomtom on 04/02/16.
 */
public class SoftBodyTestScene extends Game implements InputProcessor{

    Scene scene;
    SoftbodyActor particlesActor;
    ParticleGroup particleGroup1;

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
        ParticlesBatch softBodybatch = new ParticlesBatch(30000);
        ParticlesBatchLayer softBodyBatchLayer = new ParticlesBatchLayer(30000,softBodybatch);
        softBodyBatchLayer.setScaleFactor(1.50f);
        scene.addLayer(1,softBodyBatchLayer);
        particlesActor = new SoftbodyActor();
        scene.addActor(particlesActor);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.2f,0f);

        ParticleGroupDef particleGroupDef = new ParticleGroupDef();
        particleGroupDef.flags.add(ParticleDef.ParticleType.b2_elasticParticle);
        particleGroupDef.shape = shape;
        particleGroupDef.stride = 0.2f;
        particleGroup1 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        particleGroupDef.position.set(0f,0.2f);
        ParticleGroup particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,0.4f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,0.6f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,0.8f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,1f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,1.2f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,1.4f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,1.6f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,1.8f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,2f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,2.2f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,2.4f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,2.6f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,2.8f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,3f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,3.2f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,3.4f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,3.6f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);
        particleGroupDef.position.set(0f,3.8f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);

        shape.setAsBox(0.5f,0.5f);
        particleGroupDef.position.set(0f,4.4f);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);

        shape.setAsBox(2f,1);
        particleGroupDef.position.set(0f,-1.2f);
        particleGroupDef.flags.add(ParticleDef.ParticleType.b2_viscousParticle);
        particleGroup2 = this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);
        this.particlesActor.particleSystem.joinParticleGroups(particleGroup1,particleGroup2);



        //Container
        SpriteCacheLayer.setSize(3);
        SpriteCacheLayer containerLayer = new SpriteCacheLayer(3);
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.particleGroup1.applyLinearImpulse(new Vector2(0,20));
        return false;
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
