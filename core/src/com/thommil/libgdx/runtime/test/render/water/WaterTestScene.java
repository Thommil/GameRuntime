package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.graphics.cache.SpriteCache;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.scene.actor.physics.SensorActor;
import com.thommil.libgdx.runtime.scene.actor.physics.LogicActor;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.tools.SceneProfiler;
import finnstr.libgdx.liquidfun.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Live benchmark on Physics/Rendering loops
 *
 * Created by tomtom on 04/02/16.
 */
public class WaterTestScene extends Game implements InputProcessor,ContactListener{

    Scene scene;
    WaterActor particlesActor;
    ParticleDef particleDef;
    SensorActor gcSensor;

    boolean pouring = false;
    int stepCounter = 0;
    int dropFrequency = 5;

    private Texture duckTexture;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 10;
        settings.viewport.minWorldHeight = 10;
        settings.physics.particleIterations = 3;
        //settings.renderer.blendEnabled = false;
        //settings.physics.debug = true;
        scene = new Scene(settings);

        //Shared Cache
        //final SpriteCache spriteCache = new SpriteCache(5);

        //Background - 0
        scene.addLayer(new SpriteCacheLayer(1));
        Texture backgroundTexture = new Texture(Gdx.files.internal("floor_tiles.jpg"));
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        scene.addActor(new StaticActor(backgroundTexture,-5,-5,10,10,0,4,4,0, Color.WHITE.toFloatBits()));

        //Ducks - 1
        duckTexture = new Texture(Gdx.files.internal("duck.png"));
        scene.addLayer(new SpriteBatchLayer(100));

        //Water - 2
        scene.addLayer(new WaterLayer());
        this.particlesActor = new WaterActor(2);
        this.particleDef = new ParticleDef();
        this.particleDef.flags.add(ParticleDef.ParticleType.b2_waterParticle);
        this.particleDef.position.set(2.5f,0f);
        this.particleDef.velocity.set(0.01f,-0.1f);
        ParticleGroupDef particleGroupDef = new ParticleGroupDef();
        particleGroupDef.flags.add(ParticleDef.ParticleType.b2_waterParticle);
        PolygonShape waterShape = new PolygonShape();
        waterShape.setAsBox(2.5f,0.5f);
        particleGroupDef.position.set(0,-1f);
        particleGroupDef.shape = waterShape;
        scene.addActor(particlesActor);

        this.particlesActor.particleSystem.setParticleMaxCount(4000);
        this.particlesActor.particleSystem.createParticleGroup(particleGroupDef);

        //Tub - 3
        scene.addLayer(new SpriteCacheLayer(4));
        Texture tubTexture = new Texture(Gdx.files.internal("tub.jpg"));
        Texture tabTexture = new Texture(Gdx.files.internal("metal.png"));
        tubTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        scene.addActor(new Tub(3, tubTexture,-4,-4,8,1,0,0.5f,4,0, Color.WHITE.toFloatBits()));
        scene.addActor(new Tub(3, tubTexture,-4,-3,1,2,0,1,0.5f,0, Color.WHITE.toFloatBits()));
        scene.addActor(new Tub(3, tubTexture,3,-3,1,4,0,2,0.5f,0, Color.WHITE.toFloatBits()));
        scene.addActor(new Tub(3, tabTexture,2.25f,0f,0.75f,0.2f,0,1,1,0, Color.WHITE.toFloatBits()));

        //Sensor for GC
        this.gcSensor = new SensorActor() {
            @Override
            public List<Shape> getShapes() {
                List<Shape> shapes = new ArrayList<Shape>();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(100f,4f);
                shapes.add(shape);
                return shapes;
            }

            @Override
            public void setBody(Body body) {
                super.setBody(body);
                body.setTransform(0f,-10f,0);
                body.getFixtureList().get(0).setUserData(this);
            }
        };
        scene.addActor(this.gcSensor);

        //Step listener
        scene.addActor(new LogicActor(){
            @Override
            public void step(long lastStepDuration) {
                if(pouring) {
                    if (stepCounter % dropFrequency == 0) {
                        WaterTestScene.this.particlesActor.particleSystem.createParticle(WaterTestScene.this.particleDef);
                        stepCounter = 0;
                    }
                    stepCounter++;
                }
            }
        });

        scene.setContactListener(this);

        Gdx.input.setInputProcessor(this);

        SceneProfiler.profile(this.scene, (byte)(SceneProfiler.RENDERER | SceneProfiler.PHYSICS), 5000);

        this.setScreen(scene);
    }

    final Vector2 tmpScreenVector = new Vector2();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tmpScreenVector.set(screenX,screenY);
        final Vector2 worldVector =  this.scene.getViewport().unproject(tmpScreenVector);
        if(worldVector.x > -3f && worldVector.x < 3f
                && worldVector.y > -1){
            if(worldVector.x > 2.25f && worldVector.x < 3f
                    && worldVector.y > 0 &&  worldVector.y < 0.5f){
                this.pouring =! this.pouring;
            }
            else {
                this.scene.addActor(new DuckActor(duckTexture,1, worldVector.x, worldVector.y));
            }
        }
        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        final Actor actorA = (Actor) contact.getFixtureA().getUserData();
        final Actor actorB = (Actor) contact.getFixtureB().getUserData();
        if(actorA != null && actorB != null) {
            if (actorB != null && actorA.getId() == this.gcSensor.getId()) {
                this.scene.removeActor(actorB);
            } else if (actorA != null && actorB.getId() == this.gcSensor.getId()) {
                this.scene.removeActor(actorA);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.duckTexture.dispose();
    }

    @Override
    public void beginParticleBodyContact(ParticleSystem particleSystem, ParticleBodyContact particleBodyContact) {

    }

    @Override
    public void endContact(Contact contact) {

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
