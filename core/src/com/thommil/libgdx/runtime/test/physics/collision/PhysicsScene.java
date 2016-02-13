package com.thommil.libgdx.runtime.test.physics.collision;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.SensorActor;
import com.thommil.libgdx.runtime.scene.layer.BasicBatchLayer;
import com.thommil.libgdx.runtime.scene.listener.SceneListener;
import com.thommil.libgdx.runtime.tools.SceneProfiler;
import finnstr.libgdx.liquidfun.ParticleBodyContact;
import finnstr.libgdx.liquidfun.ParticleContact;
import finnstr.libgdx.liquidfun.ParticleSystem;

/**
 * Contacts listener tests (code not optimized to stay simple !!!)
 *
 * //TODO Buggy collison on sync mode, LibGDX bug ???
 *
 * Created by tomtom on 04/02/16.
 */
public class PhysicsScene extends Game implements SceneListener, InputProcessor, ContactListener{

    Scene defaultScene;
    Texture textureCuriosity;
    Texture textureExplosion;
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
        textureCuriosity = new Texture(Gdx.files.internal("curiosity.png"));
        textureExplosion = new Texture(Gdx.files.internal("explosion.png"));

        //Layer
        defaultScene.addLayer(0, new BasicBatchLayer(2));
        defaultScene.addLayer(1, new BasicBatchLayer(1000));

        //Actors

        defaultScene.addActor(new StaticPhysicsActor());

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50f,20f);
        SensorActor burnSensor = new SensorActor(shape);
        defaultScene.addActor(burnSensor);
        burnSensor.body.setTransform(0f,-20f,0);

        this.defaultScene.setSceneListener(this);
        this.defaultScene.setContactListener(this);

        SceneProfiler.profile(this.defaultScene);

        Gdx.input.setInputProcessor(this);

        this.setScreen(defaultScene);
    }

    @Override
    public void onStep(long lastDuration) {
        if (inc % 10 == 0) {
            defaultScene.addActor(new DynamicPhysicsActor(textureCuriosity));
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
    public void beginContact(Contact contact) {
        final Actor actorA = (Actor) contact.getFixtureA().getUserData();
        final Actor actorB = (Actor) contact.getFixtureB().getUserData();
        //Sensor
        if(actorA instanceof DynamicPhysicsActor && actorB instanceof SensorActor){
            ((DynamicPhysicsActor) actorA).texture = textureExplosion;
        }
        else if(actorB instanceof DynamicPhysicsActor && actorA instanceof SensorActor){
            ((DynamicPhysicsActor) actorB).texture = textureExplosion;
        }

        //Collision
        else if(actorA instanceof DynamicPhysicsActor && actorB instanceof StaticPhysicsActor){
            final ExplosionActor explosionActor = new ExplosionActor(textureExplosion);
            explosionActor.setPosition(((DynamicPhysicsActor) actorA).x,((DynamicPhysicsActor) actorA).y-2f);
            this.defaultScene.addActor(explosionActor);
            this.defaultScene.removeActor(actorA);
        }
        else if(actorB instanceof DynamicPhysicsActor && actorA instanceof StaticPhysicsActor){
            final ExplosionActor explosionActor = new ExplosionActor(textureExplosion);
            explosionActor.setPosition(((DynamicPhysicsActor) actorB).x,((DynamicPhysicsActor) actorB).y-2f);
            this.defaultScene.addActor(explosionActor);
            this.defaultScene.removeActor(actorB);
        }
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
