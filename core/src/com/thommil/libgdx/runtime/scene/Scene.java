package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Main container for Actors.
 *
 * Created by thommil on 01/02/16.
 */
public class Scene implements Screen {

    /**
     * Scene settings
     */
    public final Scene.Settings settings;

    /**
     * Viewport of the scene
     */
    private final Viewport viewport;

    /**
     * Current camera
     */
    private final OrthographicCamera camera;

    /**
     * Map of all actors of the scene
     */
    protected final IntMap<Actor> actorsMap;

    /**
     * Inner layers  list
     */
    protected final Array<Layer> layers;

    /**
     * The current number of renderable in the scene
     */
    protected int renderablesCount;

    /**
     * Executor bound to this scene
     */
    protected ExecutorService executor;

    /**
     * Physics World container
     */
    protected final World physicsWorld;

    /**
     * Queue for step executor
     */
    private final Deque<Runnable> physicsQueue;

    /**
     * Lock for rendering sync
     */
    private final Lock renderLock = new ReentrantLock();

    /**
     * List of stepable actors
     */
    protected final Array<Stepable> stepables;

    /**
     * Duration of last physics step processing in ms
     */
    private long lastPhysicsStepDuration;

    /**
     * Debug renderer for physics
     */
    private Box2DDebugRenderer debugRenderer;

    /**
     * Pause/resume state
     */
    protected boolean paused;

    /**
     * Default constructor using Settings
     *
     * @param settings The Scene settings
     */
    public Scene(final Scene.Settings settings) {
        //Gdx.app.debug("Scene","New scene");
        this.settings = settings;
        this.paused = true;
        this.actorsMap = new IntMap<Actor>(this.settings.core.initialCapacity);

        //Physics
        this.physicsWorld = new World(new Vector2(settings.physics.gravity[0], settings.physics.gravity[1]), true);
        this.stepables = new Array<Stepable>(false, this.settings.core.initialCapacity);
        this.physicsQueue = new ArrayDeque<Runnable>();

        //Graphics
        this.layers = new Array<Layer>();
        this.renderablesCount = 0;
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(settings.viewport.minWorldWidth,settings.viewport.minWorldHeight,this.camera);
        this.viewport.apply(settings.viewport.centerCamera);

        //GL Settings
        Gdx.gl.glClearColor(this.settings.renderer.clearColor[0]
                , this.settings.renderer.clearColor[1]
                , this.settings.renderer.clearColor[2]
                , this.settings.renderer.clearColor[3]);

        Gdx.gl.glDepthMask(this.settings.renderer.depthMaskEnabled);

        if (this.settings.renderer.blendEnabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(this.settings.renderer.blendSrcFunc, this.settings.renderer.blendDstFunc);
        } else {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    /**
     * Adds a layer to scene at the specified index
     *
     * @oaram index The layer index
     * @param layer The layer to add
     *
     * @return The layer index
     */
    public void addLayer(final int index, final Layer layer){
        //Gdx.app.debug("Scene","addLayer("+index+")");
        layer.setCamera(this.camera);
        this.renderLock.lock();
        while(index > (this.layers.size-1)){
            this.layers.add(null);
        }
        this.layers.set(index, layer);
        this.renderLock.unlock();
    }

    /**
     * Adds a layer to scene
     *
     * @param layer The layer to add
     *
     * @return The layer index
     */
    public int addLayer(final Layer layer){
        //Gdx.app.debug("Scene","addLayer()");
        layer.setCamera(this.camera);
        this.renderLock.lock();
        this.layers.add(layer);
        this.renderLock.unlock();
        return (this.layers.size-1);
    }

    /**
     * Removes a Layer from the Scene
     *
     * @param index The Layer index to remove
     */
    public void removeLayer(final int index){
        this.renderLock.lock();
        this.layers.set(index, null);
        this.renderLock.unlock();
    }

    /**
     * Enables/shows a layer
     *
     * @param index The index of the layer
     */
    public void showLayer(final int index){
        //Gdx.app.debug("Scene","showLayer("+index+")");
        this.layers.get(index).show();
    }

    /**
     * Disables/hides a layer
     *
     * @param index The index of the layer
     */
    public void hideLayer(final int index){
        //Gdx.app.debug("Scene","hideLayer("+index+")");
        this.layers.get(index).hide();
    }

    /**
     * Adds an actor to the Scene
     *
     * @param actor The Actor to add
     */
    public void addActor(final Actor actor) {
        //Gdx.app.debug("Scene","addActor()");
        if(!this.paused) {
            this.runOnPhysicsThread(new Runnable() {
                @Override
                public void run() {
                    if(actor instanceof Renderable) {
                        Scene.this.renderLock.lock();
                        Scene.this.layers.get(((Renderable) actor).getLayer()).add((Renderable) actor);
                        Scene.this.renderablesCount++;
                        Scene.this.renderLock.unlock();
                    }
                    if(actor instanceof RigidBody) {
                        final RigidBody rigidBody = (RigidBody)actor;
                        final BodyDef bodyDef = new BodyDef();
                        rigidBody.setDefinition(bodyDef);
                        final Body body = Scene.this.physicsWorld.createBody(bodyDef);
                        for(final Shape shape : rigidBody.getShapes()){
                            final Fixture fixture = body.createFixture(shape, rigidBody.getDensity());
                            shape.dispose();
                        }
                        rigidBody.setBody(body);
                    }
                    else if(actor instanceof ParticlesBody) {
                        final ParticlesBody particlesBody = (ParticlesBody)actor;
                        final ParticleSystemDef particleSystemDef = new ParticleSystemDef();
                        particlesBody.setDefinition(particleSystemDef);
                        final ParticleSystem particleSystem = new ParticleSystem(Scene.this.physicsWorld,particleSystemDef);
                        particlesBody.setBody(particleSystem);
                    }
                    if(actor instanceof Stepable){
                        Scene.this.stepables.add((Stepable)actor);
                    }
                    Scene.this.actorsMap.put(actor.getId(),actor);
                }
            });
        }
        else{
            if(actor instanceof Renderable) {
                this.layers.get(((Renderable) actor).getLayer()).add((Renderable) actor);
                this.renderablesCount++;
            }
            if(actor instanceof RigidBody) {
                final RigidBody rigidBody = (RigidBody)actor;
                final BodyDef bodyDef = new BodyDef();
                rigidBody.setDefinition(bodyDef);
                final Body body = this.physicsWorld.createBody(bodyDef);
                for(final Shape shape : rigidBody.getShapes()){
                    final Fixture fixture = body.createFixture(shape, rigidBody.getDensity());
                    shape.dispose();
                }
                rigidBody.setBody(body);
            }
            else if(actor instanceof ParticlesBody) {
                final ParticlesBody particlesBody = (ParticlesBody)actor;
                final ParticleSystemDef particleSystemDef = new ParticleSystemDef();
                particlesBody.setDefinition(particleSystemDef);
                final ParticleSystem particleSystem = new ParticleSystem(this.physicsWorld,particleSystemDef);
                particlesBody.setBody(particleSystem);
            }
            if(actor instanceof Stepable){
                this.stepables.add((Stepable)actor);
            }
            this.actorsMap.put(actor.getId(),actor);
        }
    }

    /**
     * Gets ans actor from its ID
     *
     * @param id The ID of the Actor
     * @return The actor if found, null otherwise
     */
    public Actor getActor(final int id){
        return this.actorsMap.get(id);
    }

    /**
     * Removes an actor from the Scene
     *
     * @param actor The Actor to remove
     */
    public void removeActor(final Actor actor){
        //Gdx.app.debug("Scene","removeActor()");
        if(!this.paused) {
            this.runOnPhysicsThread(new Runnable() {
                @Override
                public void run() {
                    if (actor instanceof Renderable) {
                        Scene.this.renderLock.lock();
                        Scene.this.layers.get(((Renderable) actor).getLayer()).remove((Renderable) actor);
                        Scene.this.renderablesCount--;
                        Scene.this.renderLock.unlock();
                    }
                    if (actor instanceof Stepable) {
                        Scene.this.stepables.removeValue((Stepable)actor, false);
                    }
                    Scene.this.actorsMap.remove(actor.getId());
                    actor.dispose();
                }
            });
        }
        else{
            if (actor instanceof Renderable) {
                this.layers.get(((Renderable) actor).getLayer()).remove((Renderable) actor);
                this.renderablesCount--;
            }
            if (actor instanceof Stepable) {
                this.stepables.removeValue((Stepable)actor, false);
            }
            this.actorsMap.remove(actor.getId());
            actor.dispose();
        }
    }

    @Override
    public void show() {
        //Gdx.app.debug("Scene","show()");
        if(settings.physics.debug){
            this.debugRenderer = new Box2DDebugRenderer();
        }

        for(final Layer layer : this.layers) {
            if(layer != null) {
                layer.show();
            }
        }

        //Start executor
        this.executor = Executors.newFixedThreadPool(this.settings.core.executors, new ThreadFactory() {
            @Override
            public Thread newThread (Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });

        //Aync loop
        this.executor.execute(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    final long longAsyncFrequency = (long)(Scene.this.settings.physics.frequency * 1000f);
                    final long start = System.currentTimeMillis();

                    if (!Scene.this.paused) {

                        while(!Scene.this.physicsQueue.isEmpty()){
                            Scene.this.physicsQueue.poll().run();
                        }

                        for (final Stepable actor : Scene.this.stepables) {
                            actor.step(lastPhysicsStepDuration);
                        }

                        Scene.this.physicsWorld.step(Scene.this.settings.physics.frequency * Scene.this.settings.physics.timeFactor
                                , Scene.this.settings.physics.velocityIterations
                                , Scene.this.settings.physics.positionIterations
                                , Scene.this.settings.physics.particleIterations);

                    }
                    lastPhysicsStepDuration = System.currentTimeMillis() - start;

                    if (lastPhysicsStepDuration < longAsyncFrequency) {
                        try {
                            Thread.currentThread().sleep(longAsyncFrequency - lastPhysicsStepDuration);
                        } catch (InterruptedException ie) {
                            Gdx.app.exit();
                        }
                    }
                }
            }
        });
        this.paused = false;
    }

    /**
     * Post a runnable task to be executed at start of physics loop (async mode only)
     *
     * @param runnable The task to be executed
     */
    public void runOnPhysicsThread(final Runnable runnable){
        this.physicsQueue.add(runnable);
    }

    /**
     * Post a runnable task to be executed at start of UI loop
     *
     * @param runnable The task to be executed
     */
    public void runOnUIThread(final Runnable runnable){
        Gdx.app.postRunnable(runnable);
    }

    @Override
    public void render(float delta) {
        if(this.settings.renderer.clearScreen) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        this.renderLock.lock();

        for (final Layer layer : this.layers) {
            if (layer != null) {
                layer.render(delta);
            }
        }
        if(this.settings.physics.debug){
            debugRenderer.render(this.physicsWorld, this.camera.combined);
        }
        this.renderLock.unlock();

    }

    /**
     * Set the current contact listener
     *
     * @param contactListener The current ContactListener
     */
    public void setContactListener(final ContactListener contactListener){
        this.physicsWorld.setContactListener(contactListener);
    }

    @Override
    public void resize(int width, int height) {
        //Gdx.app.debug("Scene","resize("+width+", "+height+")");
        this.viewport.update(width,height);
        for(final Layer layer : this.layers) {
            if(layer != null) {
                layer.resize(width, height);
            }
        }
    }

    @Override
    public void pause() {
        //Gdx.app.debug("Scene","pause()");
        this.paused = true;
    }

    @Override
    public void resume() {
        //Gdx.app.debug("Scene","resume()");
        this.paused = false;
    }

    @Override
    public void hide() {
        //Gdx.app.debug("Scene","hide()");
        this.dispose();
    }

    @Override
    @SuppressWarnings("all")
    public void dispose() {
        //Gdx.app.debug("Scene","dispose()");
        this.paused = true;
        for(final Actor actor : this.actorsMap.values()){
            actor.dispose();
        }
        for(final Layer layer : this.layers){
            if(layer != null) {
                layer.dispose();
            }
        }
        this.executor.shutdown();
    }

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public boolean isPaused() {
        return paused;
    }

    public int getRenderablesCount() {
        return renderablesCount;
    }

    /**
     * Raw object to define scene properties
     */
    public static class Settings{

        /**
         * Core settings
         */
        public Core core = new Core();

        /**
         * Viewport settings
         */
        public Viewport viewport = new Viewport();

        /**
         * Physics settings
         */
        public Physics physics = new Physics();

        /**
         * Gravity settings
         */
        public Renderer renderer = new Renderer();

        /**
         * Core settings class
         */
        public static class Core{
            public int executors = 1;
            public int initialCapacity = 100;
        }

        /**
         * Viewport class for Scene settings.<br/>
         * To be efficient with physics engine, think in meters :)
         */
        public static class Viewport{
            public float minWorldWidth = Gdx.graphics.getWidth();
            public float minWorldHeight = Gdx.graphics.getHeight();
            public boolean centerCamera = true;
        }

        /**
         * Renderer class for Scene settings
         */
        public static class Renderer{
            public boolean clearScreen = true;
            public float[] clearColor = {0f, 0f, 0f, 1f};
            public boolean depthMaskEnabled = false;
            public boolean blendEnabled = true;
            public int blendSrcFunc = GL20.GL_SRC_ALPHA;
            public int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
        }

        /**
         * Physics class for Scene settings
         */
        public static class Physics{
            public float[] gravity = {0.0f,-9.8f};
            public float frequency = 1/100f;
            public float timeFactor = 1f;
            public int velocityIterations = 8;
            public int positionIterations = 3;
            public int particleIterations = 3;
            public boolean debug = false;
        }


    }
}
