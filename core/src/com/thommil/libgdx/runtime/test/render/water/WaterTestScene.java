package com.thommil.libgdx.runtime.test.render.water;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.scene.actor.physics.SensorActor;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
import com.thommil.libgdx.runtime.scene.listener.SceneListener;
import com.thommil.libgdx.runtime.tools.SceneProfiler;
import finnstr.libgdx.liquidfun.ParticleBodyContact;
import finnstr.libgdx.liquidfun.ParticleContact;
import finnstr.libgdx.liquidfun.ParticleDef;
import finnstr.libgdx.liquidfun.ParticleSystem;

import java.util.ArrayList;
import java.util.List;


/**
 * Live benchmark on Physics/Rendering loops
 *
 * Created by tomtom on 04/02/16.
 */
public class WaterTestScene extends Game implements InputProcessor, SceneListener,ContactListener{

    Scene scene;
    WaterActor particlesActor;
    ParticleDef particleDef;
    SensorActor gcSensor;

    boolean pouring = false;
    int stepCounter = 0;
    int dropFrequency = 5;

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

        //Background
        scene.addLayer(new SpriteCacheLayer(1));
        Texture backgroundTexture = new Texture(Gdx.files.internal("floor_tiles.jpg"));
        backgroundTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        scene.addActor(new StaticActor(backgroundTexture,-5,-5,10,10,0,4,4,0, Color.WHITE.toFloatBits()));

        //Water
        scene.addLayer(new WaterLayer());
        particlesActor = new WaterActor(1);
        scene.addActor(particlesActor);

        this.particleDef = new ParticleDef();
        this.particleDef.flags.add(ParticleDef.ParticleType.b2_waterParticle);
        this.particleDef.position.set(2.5f,0f);
        this.particleDef.velocity.set(0.01f,-0.1f);
        this.particlesActor.particleSystem.setParticleMaxCount(4000);

        //Tub
        scene.addLayer(new SpriteBatchLayer(100));
        Texture tubTexture = new Texture(Gdx.files.internal("tub.jpg"));
        Texture tabTexture = new Texture(Gdx.files.internal("metal.png"));
        tubTexture.setWrap(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        scene.addActor(new Tub(2, tubTexture,-4,-4,8,1,0,0.5f,4,0, Color.WHITE.toFloatBits()));
        scene.addActor(new Tub(2, tubTexture,-4,-3,1,2,0,1,0.5f,0, Color.WHITE.toFloatBits()));
        scene.addActor(new Tub(2, tubTexture,3,-3,1,4,0,2,0.5f,0, Color.WHITE.toFloatBits()));
        scene.addActor(new Tub(2, tabTexture,2.25f,0f,0.75f,0.2f,0,1,1,0, Color.WHITE.toFloatBits()));

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

        scene.setSceneListener(this);
        scene.setContactListener(this);

        Gdx.input.setInputProcessor(this);

        SceneProfiler.profile(this.scene, (byte)(SceneProfiler.RENDERER | SceneProfiler.PHYSICS), 5000);

        this.setScreen(scene);
    }

    /**
     * Called before each worl step with the duration of the last step processing
     *
     * @param lastDuration The duration of the last processing for QoS purpose
     */
    @Override
    public void onStep(long lastDuration) {
        if(pouring) {
            if (stepCounter % dropFrequency == 0) {
                this.particlesActor.particleSystem.createParticle(this.particleDef);
                stepCounter = 0;
            }
            stepCounter++;
        }
    }

    final Vector2 tmpScreenVector = new Vector2();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tmpScreenVector.set(screenX,screenY);
        final Vector2 worldVector =  this.scene.getViewport().unproject(tmpScreenVector);
        if(worldVector.x > -10f && worldVector.x < 3f
                && worldVector.y > -1){
            if(worldVector.x > 2.25f && worldVector.x < 3f
                    && worldVector.y > 0 &&  worldVector.y < 0.5f){
                this.pouring =! this.pouring;
            }
            else {
                this.scene.addActor(new DuckActor(2, worldVector.x, worldVector.y));
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
    public void beginParticleBodyContact(ParticleSystem particleSystem, ParticleBodyContact particleBodyContact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    /**
     * Called before each rendering
     *
     * @param deltaTime The time ellapsed since last rendering
     */
    @Override
    public void onRender(float deltaTime) {

    }

    /**
     * Called on resize()
     *
     * @param width
     * @param height
     */
    @Override
    public void onResize(int width, int height) {

    }

    /**
     * Called on show()
     */
    @Override
    public void onShow() {

    }

    /**
     * Called on hide()
     */
    @Override
    public void onHide() {

    }

    /**
     * Called on resume()
     */
    @Override
    public void onResume() {

    }

    /**
     * Called on pause()
     */
    @Override
    public void onPause() {

    }

    /**
     * called on dispose()
     */
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
