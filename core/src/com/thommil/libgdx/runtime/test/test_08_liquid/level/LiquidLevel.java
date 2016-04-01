package com.thommil.libgdx.runtime.test.test_08_liquid.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.actor.graphics.StaticActor;
import com.thommil.libgdx.runtime.actor.physics.*;
import com.thommil.libgdx.runtime.graphics.TextureSet;
import com.thommil.libgdx.runtime.layer.CacheLayer;
import com.thommil.libgdx.runtime.layer.OffScreenLayer;
import com.thommil.libgdx.runtime.layer.ParticlesBatchLayer;
import com.thommil.libgdx.runtime.layer.SpriteBatchLayer;
import com.thommil.libgdx.runtime.tools.RuntimeProfiler;
import finnstr.libgdx.liquidfun.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Thommil on 04/03/16.
 */
public class LiquidLevel implements InputProcessor, ContactListener ,Disposable {

    private CacheLayer bgCacheLayer;
    private CacheLayer tubCacheLayer;
    private SpriteBatchLayer spriteBatchLayer;
    private OffScreenLayer<ParticlesBatchLayer> waterLayer;

    private TextureSet bgTextureSet;
    private TextureSet duckTextureSet;
    private TextureSet tubTextureSet;
    private TextureSet tabTextureSet;

    private WaterActor waterActor;
    private HeadlessBodyActor garbageCollectorActor;

    public LiquidLevel() {
        CacheLayer.setGlobalSize(10);
        SpriteBatchLayer.setGlobalSize(100);
        ParticlesBatchLayer.setGlobalSize(5000);

        //Background - 0
        bgCacheLayer = new CacheLayer(Runtime.getInstance().getViewport(), 1);
        bgTextureSet = new TextureSet(new Texture("static/wall_tiles.png"));
        bgTextureSet.setWrapAll(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        bgCacheLayer.addActor(new StaticActor(0,bgTextureSet,-5f,-5f,10f,10f,0f,1f,1f,0f, Color.WHITE.toFloatBits()));
        Runtime.getInstance().addLayer(bgCacheLayer);

        //Ducks - 1
        spriteBatchLayer = new SpriteBatchLayer(Runtime.getInstance().getViewport(),100);
        duckTextureSet = new TextureSet(new Texture("sprites/duck.png"));
        Runtime.getInstance().addLayer(spriteBatchLayer);

        //Water - 2
        final ParticlesBatchLayer particlesBatchLayer = new ParticlesBatchLayer(Runtime.getInstance().getViewport(), 5000);
        particlesBatchLayer.setScaleFactor(4);
        waterLayer = new OffScreenLayer<ParticlesBatchLayer>(Runtime.getInstance().getViewport(), particlesBatchLayer, new WaterRenderer(Runtime.getInstance().getViewport()));
        waterActor = new WaterActor();
        waterLayer.addActor(waterActor);
        Runtime.getInstance().addLayer(waterLayer);
        waterActor.createGroup(0,-1f, 5f, 1f);

        //Tub - 3
        tubCacheLayer = new CacheLayer(Runtime.getInstance().getViewport(), 9);
        tubTextureSet = new TextureSet(new Texture("static/tub_tiles.png"));
        tabTextureSet = new TextureSet(new Texture("static/metal.png"));
        tubTextureSet.setWrapAll(Texture.TextureWrap.Repeat,Texture.TextureWrap.Repeat);
        tubCacheLayer.addActor(new Tub(tubTextureSet,-4,-4,8,1,0,0.5f,4,0, Color.WHITE.toFloatBits()));
        tubCacheLayer.addActor(new Tub(tubTextureSet,-4,-3,1,2,0,1,0.5f,0, Color.WHITE.toFloatBits()));
        tubCacheLayer.addActor(new Tub(tubTextureSet,3,-3,1,4,0,2,0.5f,0, Color.WHITE.toFloatBits()));
        tubCacheLayer.addActor(new Tub(tabTextureSet,2.25f,0f,0.75f,0.2f,0,1,1,0, Color.WHITE.toFloatBits()));
        Runtime.getInstance().addLayer(tubCacheLayer);


        //Duck garbage collector bind to bg layer
        garbageCollectorActor = new HeadlessBodyActor(MathUtils.random(0x7ffffffe)) {

            @Override
            public Array<FixtureDef> getFixturesDefinition() {
                Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(100f,4f);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.density = 1.0f;
                fixtureDef.shape = shape;
                fixtureDefs.add(fixtureDef);
                return fixtureDefs;
            }

            @Override
            public void setBody(Body body) {
                super.setBody(body);
                body.setTransform(0f,-10f,0);
                body.getFixtureList().get(0).setUserData(this);
            }
        };
        bgCacheLayer.addActor(garbageCollectorActor);

        RuntimeProfiler.profile();
        Gdx.input.setInputProcessor(this);
        Runtime.getInstance().getPhysicsWorld().setContactListener(this);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose() {
        this.bgCacheLayer.dispose();
        this.spriteBatchLayer.dispose();
        this.tubCacheLayer.dispose();
        this.waterLayer.dispose();

        this.bgTextureSet.dispose();
        this.duckTextureSet.dispose();
        this.tubTextureSet.dispose();
        this.tabTextureSet.dispose();

        this.garbageCollectorActor.dispose();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        final Vector2 worldVector =  Runtime.getInstance().getViewport().unproject(new Vector2(screenX,screenY));
        if(worldVector.x > -3f && worldVector.x < 3f && worldVector.y > -1){
            if(worldVector.x > 2.25f && worldVector.x < 3f && worldVector.y > 0 &&  worldVector.y < 0.5f){
                waterActor.switchPouring();
            }
            else {
                spriteBatchLayer.addActor(new DuckActor(duckTextureSet,worldVector.x, worldVector.y));
            }
        }
        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        final Actor actorA = (Actor) contact.getFixtureA().getBody().getUserData();
        final Actor actorB = (Actor) contact.getFixtureB().getBody().getUserData();
        if(actorA != null && actorB != null) {
            if (actorB != null && actorA.getId() == this.garbageCollectorActor.getId()) {
                this.spriteBatchLayer.removeActor(actorB);
            } else if (actorA != null && actorB.getId() == this.garbageCollectorActor.getId()) {
                this.spriteBatchLayer.removeActor(actorA);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }


    public static class Tub extends StaticBodyActor{

        public Tub(TextureSet textureSet, float x, float y, float width, float height, float u, float v, float u2, float v2, float color) {
            super(MathUtils.random(0x7ffffffe), textureSet, x, y, width, height, u, v, u2, v2, color);
        }


        @Override
        public Array<FixtureDef> getFixturesDefinition() {
            Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(this.width/2 - 0.05f,this.height/2, new Vector2(this.width/2,this.height/2 - 0.05f),0f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 1.0f;
            fixtureDef.shape = shape;
            fixtureDefs.add(fixtureDef);
            return fixtureDefs;
        }
    }

    public static class DuckActor extends SpriteBodyActor {


        public DuckActor(TextureSet textureSet, float x, float y) {
            super(MathUtils.random(0x7ffffffe), textureSet);
            this.setSize(0.5f,0.5f);
            this.setOriginCenter();
            this.setPosition(x, y);
        }

        @Override
        public Array<FixtureDef> getFixturesDefinition() {
            Array<FixtureDef> fixtureDefs = new Array<FixtureDef>();
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(0.25f);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.density = 0.4f;
            fixtureDef.shape = circleShape;
            fixtureDefs.add(fixtureDef);
            return fixtureDefs;
        }

        /**
         * Set body instance of the Collidable
         *
         * @param body
         */
        @Override
        public void setBody(Body body) {
            super.setBody(body);
            MassData massData = body.getMassData();
            massData.center.set(0,-0.1f);
            body.setMassData(massData);
        }
    }

    public static class WaterActor extends ParticleSystemActor implements Stepable{

        boolean pouring = false;
        int stepCounter = 0;
        int dropFrequency = 1;

        final ParticleDef particleDef;

        public WaterActor() {
            super(MathUtils.random(0x7ffffffe), 0.05f);
            particleDef = new ParticleDef();
            this.particleDef.flags.add(ParticleDef.ParticleType.b2_waterParticle);
        }

        /**
         * Gets the definition of Collidable.
         *
         * @return definition The collidable definition (settings)
         */
        @Override
        public ParticleSystemDef getDefinition() {
            final ParticleSystemDef particleSystemDef = super.getDefinition();
            particleSystemDef.density = 1.0f;
            return particleSystemDef;
        }

        public void createGroup(float x, float y, float w, float h){
            ParticleGroupDef particleGroupDef = new ParticleGroupDef();
            particleGroupDef.flags.add(ParticleDef.ParticleType.b2_waterParticle);
            PolygonShape waterShape = new PolygonShape();

            particleGroupDef.position.set(x,y);
            waterShape.setAsBox(w/2,h/2);
            particleGroupDef.shape = waterShape;

            this.particleSystem.setParticleMaxCount(4000);
            this.particleSystem.createParticleGroup(particleGroupDef);
        }

        public void createParticle(float x, float y, float velocityX, float velocityY){
            this.particleDef.position.set(x,y);
            this.particleDef.velocity.set(velocityX, velocityY);
            this.particleSystem.createParticle(this.particleDef);
        }

        public void switchPouring(){
            this.pouring = !this.pouring;
        }

        /**
         * Called at each physics step, any physics related task should be
         * handled here and not in the rendering phase.
         *
         * @param deltaTime the time span between the current step and the last step in seconds.
         */
        @Override
        public void step(float deltaTime) {
            if(pouring) {
                if (stepCounter % dropFrequency == 0) {
                    createParticle(2.5f,0f,0.01f,-0.1f);
                    stepCounter = 0;
                }
                stepCounter++;
            }
        }

        /**
         * Releases all resources of this object.
         */
        @Override
        public void dispose() {

        }
    }



    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */


    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
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
}
