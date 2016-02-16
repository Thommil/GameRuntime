package com.thommil.libgdx.runtime.test.physics.collision;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;
import com.thommil.libgdx.runtime.scene.Actor;
import com.thommil.libgdx.runtime.scene.Scene;
import com.thommil.libgdx.runtime.scene.actor.physics.SensorActor;
import com.thommil.libgdx.runtime.scene.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.scene.layer.SpriteCacheLayer;
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
public class CollisionTestScene extends Game implements SceneListener, InputProcessor, ContactListener{

    Scene scene;
    Texture textureCuriosity;
    Texture textureExplosion;

    int inc = 0;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_INFO);

        //Scene
        Scene.Settings settings = new Scene.Settings();
        settings.viewport.minWorldWidth = 100;
        settings.viewport.minWorldHeight = 100;
        //settings.physics.debug = true;
        scene = new Scene(settings);
        textureCuriosity = new Texture(Gdx.files.internal("curiosity.png"));
        textureExplosion = new Texture(Gdx.files.internal("explosion.png"));

        //Layer
        scene.addLayer(new SpriteCacheLayer(1));
        scene.addLayer(new SpriteBatchLayer(1000));

        //Actors

        scene.addActor(new GroundActor(new Texture(Gdx.files.internal("metal.png")),-1000f,-50f,2000f,10f));
        SensorActor burnSensor = new SensorActor() {
            @Override
            public Shape getShape() {
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(50f,20f);
                return shape;
            }
            @Override
            public void setBody(Body body) {
                super.setBody(body);
                body.setTransform(0f,-20f,0);
            }
        };
        scene.addActor(burnSensor);

        this.scene.setSceneListener(this);
        this.scene.setContactListener(this);

        SceneProfiler.profile(this.scene);

        Gdx.input.setInputProcessor(this);

        this.setScreen(scene);
    }

    @Override
    public void onStep(long lastDuration) {
        if (inc % 10 == 0) {
            scene.addActor(new CuriosityActor(textureCuriosity));
        }
        inc+=1;
    }

    @Override
    public void onRender(float deltaTime) {

    }

    @Override
    public void onResize(int width, int height) {

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
        final Actor actorA = (Actor) contact.getFixtureA().getBody().getUserData();
        final Actor actorB = (Actor) contact.getFixtureB().getBody().getUserData();
        //Sensor
        if(actorA instanceof CuriosityActor && actorB instanceof SensorActor){
            ((CuriosityActor) actorA).texture = textureExplosion;
        }
        else if(actorB instanceof CuriosityActor && actorA instanceof SensorActor){
            ((CuriosityActor) actorB).texture = textureExplosion;
        }

        //Collision
        else if(actorA instanceof CuriosityActor && actorB instanceof GroundActor){
            final ExplosionActor explosionActor = new ExplosionActor(textureExplosion);
            explosionActor.setPosition(((CuriosityActor) actorA).x,((CuriosityActor) actorA).y-2f);
            this.scene.addActor(explosionActor);
            this.scene.removeActor(actorA);
        }
        else if(actorB instanceof CuriosityActor && actorA instanceof GroundActor){
            final ExplosionActor explosionActor = new ExplosionActor(textureExplosion);
            explosionActor.setPosition(((CuriosityActor) actorB).x,((CuriosityActor) actorB).y-2f);
            this.scene.addActor(explosionActor);
            this.scene.removeActor(actorB);
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
