package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.graphics.Renderable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
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
@SuppressWarnings("unused")
public class Scene implements Screen {

    /**
     * Inner constants for async mode update
     */
    private final static int WORLD_TO_ACTOR = 0;
    private final static int ACTOR_TO_RENDERER = 1;

    /**
     * Scene settings
     */
    private final Scene.Settings settings;

    /**
     * Viewport of the scene
     */
    private final Viewport viewport;

    /**
     * Current camera
     */
    private final OrthographicCamera camera;

    /**
     * Inner layers  list
     */
    protected final Layer[] layers;

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
     * Lock for physics sync
     */
    private final Lock physicsLock = new ReentrantLock();

    /**
     * Lock for rendering sync
     */
    private final Lock renderLock = new ReentrantLock();

    /**
     * List of physics actors
     */
    protected final List<PhysicsActor> physicsActors;

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
     * The current bound SceneListener
     */
    protected SceneListener sceneListener;

    /**
     * Default constructor using Settings
     *
     * @param settings The Scene settings
     */
    @SuppressWarnings("all")
    public Scene(final Scene.Settings settings) {
        Gdx.app.debug("Scene","New scene");
        this.settings = settings;

        this.physicsWorld = new World(new Vector2(settings.physics.gravity[0], settings.physics.gravity[1]), true);
        this.physicsActors = new ArrayList<PhysicsActor>();
        this.physicsQueue = new ArrayDeque<Runnable>();

        this.layers = new Layer[settings.renderer.layers];
        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(settings.viewport.minWorldWidth,settings.viewport.minWorldHeight,this.camera);
        this.viewport.apply(settings.viewport.centerCamera);
    }

    /**
     *  Autotuning method
     */
    private void autotune(){
        switch(Gdx.graphics.getFramesPerSecond()/10) {
            //50+ FPS - Full Throttle
            case 6:
            case 5:
                this.settings.physics.frequency = 0.01666666666f;
                break;
            //Init phase
            case 0:
                this.settings.physics.asyncMode = !Gdx.app.getType().equals(Application.ApplicationType.Android);
                this.settings.physics.frequency = 0.01666666666f;
                this.settings.physics.velocityIterations = 8;
                this.settings.physics.positionIterations = 3;
                this.settings.physics.particleIterations = 1;
                break;
            default:
                this.settings.physics.frequency = 0.03333333333f;
        }
    }

    /**
     * Adds a layer to scene (beware of the ascending order !!!)
     *
     * @param layer The layer to add
     */
    public void setLayer(final int index, final Layer layer){
        Gdx.app.debug("Scene","setLayer("+index+")");
        layer.setCamera(this.camera);
        this.layers[index] = layer;
    }

    /**
     * Enables/shows a layer
     *
     * @param index The index of the layer
     */
    public void showLayer(final int index){
        Gdx.app.debug("Scene","showLayer("+index+")");
        this.layers[index].show();
    }

    /**
     * Disables/hides a layer
     *
     * @param index The index of the layer
     */
    public void hideLayer(final int index){
        Gdx.app.debug("Scene","hideLayer("+index+")");
        this.layers[index].hide();
    }

    /**
     * Adds an actor to the Scene
     *
     * @param actor The Actor to add
     */
    public void addActor(final Actor actor) {
        Gdx.app.debug("Scene","addActor()");
        if(this.settings.physics.asyncMode) {
            this.renderLock.lock();
            if(actor instanceof Renderable) {
                this.layers[((Renderable) actor).getLayer()].addRenderable((Renderable) actor);
            }
            if(actor instanceof PhysicsActor) {
                this.physicsLock.lock();
                ((PhysicsActor) actor)._init(this.physicsWorld);
                ((PhysicsActor) actor).physicsComponents[Actor.X] = ((PhysicsActor) actor).body.getPosition().x;
                ((PhysicsActor) actor).physicsComponents[Actor.Y] = ((PhysicsActor) actor).body.getPosition().y;
                ((PhysicsActor) actor).physicsComponents[Actor.ANGLE] = ((PhysicsActor) actor).body.getAngle();
                this.physicsActors.add(((PhysicsActor) actor));
                this.physicsLock.unlock();
            }
            this.renderLock.unlock();
        }
        else{
            if(actor instanceof Renderable) {
                this.layers[((Renderable) actor).getLayer()].addRenderable((Renderable) actor);
            }
            if(actor instanceof PhysicsActor) {
                ((PhysicsActor) actor)._init(this.physicsWorld);
                this.physicsActors.add(((PhysicsActor) actor));
            }
        }
    }

    /**
     * Removes an actor from the Scene
     *
     * @param actor The Actor to remove
     */
    public void removeActor(final Actor actor){
        Gdx.app.debug("Scene","removeActor()");
        if(this.settings.physics.asyncMode) {
            this.renderLock.lock();

            if (actor instanceof Renderable) {
                this.layers[((Renderable) actor).getLayer()].removeRenderable((Renderable) actor);
            }
            if (actor instanceof PhysicsActor) {
                this.physicsLock.lock();
                this.physicsActors.remove(actor);
                this.physicsWorld.destroyBody(((PhysicsActor) actor).body);
                this.physicsLock.unlock();
            }
            this.renderLock.unlock();
        }
        else{
            if (actor instanceof Renderable) {
                this.layers[((Renderable) actor).getLayer()].removeRenderable((Renderable) actor);
            }
            if (actor instanceof PhysicsActor) {
                this.physicsActors.remove(actor);
                this.physicsWorld.destroyBody(((PhysicsActor) actor).body);
            }
        }
    }

    @Override
    public void show() {
        Gdx.app.debug("Scene","show()");
        if(settings.physics.debug){
            this.debugRenderer = new Box2DDebugRenderer();
        }

        for(final Layer layer : this.layers) {
            layer.show();
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
        if(this.settings.physics.asyncMode) {
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

                            if(Scene.this.sceneListener != null){
                                Scene.this.sceneListener.onStep(lastPhysicsStepDuration);
                            }

                            Scene.this.physicsLock.lock();

                            Scene.this.physicsWorld.step(Scene.this.settings.physics.frequency
                                    , Scene.this.settings.physics.velocityIterations
                                    , Scene.this.settings.physics.positionIterations
                                    , Scene.this.settings.physics.particleIterations);

                            for (final PhysicsActor actor : Scene.this.physicsActors) {
                                if(!actor.body.getType().equals(BodyDef.BodyType.StaticBody)) {
                                    final Vector2 position = actor.body.getPosition();
                                    actor.physicsComponents[Actor.X] = position.x;
                                    actor.physicsComponents[Actor.Y] = position.y;
                                    actor.physicsComponents[Actor.ANGLE] = actor.body.getAngle();
                                }
                            }

                            Scene.this.physicsLock.unlock();
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
        }
        this.paused = false;
    }

    /**
     * Post a runnable task to be executed at start of physics loop (async mode only)
     *
     * @param runnable The task to be executed
     */
    public void runOnPhysicsThread(final Runnable runnable){
        if(this.settings.physics.asyncMode) {
            this.physicsQueue.add(runnable);
        }
        else this.runOnUIThread(runnable);
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
            Gdx.gl.glClearColor(this.settings.renderer.clearColor[0]
                                , this.settings.renderer.clearColor[1]
                                , this.settings.renderer.clearColor[2]
                                , this.settings.renderer.clearColor[3]);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        //Sync step
        if(!this.settings.physics.asyncMode) {

            if(this.sceneListener != null){
                this.sceneListener.onStep(lastPhysicsStepDuration);
            }

            final long start = System.currentTimeMillis();

            this.physicsWorld.step(this.settings.physics.frequency
                    , this.settings.physics.velocityIterations
                    , this.settings.physics.positionIterations
                    , this.settings.physics.particleIterations);

            for (final PhysicsActor actor : this.physicsActors) {
                if (!actor.body.getType().equals(BodyDef.BodyType.StaticBody)) {
                    final Vector2 position = actor.body.getPosition();
                    actor.renderComponents[Actor.X] = position.x;
                    actor.renderComponents[Actor.Y] = position.y;
                    actor.renderComponents[Actor.ANGLE] = actor.body.getAngle();
                }
            }

            lastPhysicsStepDuration = System.currentTimeMillis() - start;

            if(this.sceneListener != null){
                this.sceneListener.onRender(delta);
            }

            for (final Layer layer : this.layers) {
                if (layer.isVisible()) {
                    layer.render(delta);
                }
            }

            if(Gdx.app.getLogLevel() == Gdx.app.LOG_DEBUG){
                debugRenderer.render(this.physicsWorld, this.camera.combined);
            }
        }
        //Async step
        else {
            this.physicsLock.lock();
            for (final PhysicsActor actor : this.physicsActors) {
                if(!actor.body.getType().equals(BodyDef.BodyType.StaticBody)) {
                    actor.renderComponents[Actor.X] = actor.physicsComponents[Actor.X];
                    actor.renderComponents[Actor.Y] = actor.physicsComponents[Actor.Y];
                    actor.renderComponents[Actor.ANGLE] = actor.physicsComponents[Actor.ANGLE];
                }
            }
            this.physicsLock.unlock();

            if(this.sceneListener != null){
                this.sceneListener.onRender(delta);
            }


            this.renderLock.lock();
            for (final Layer layer : this.layers) {
                if (layer.isVisible()) {
                    layer.render(delta);
                }
            }
            if(this.settings.physics.debug){
                debugRenderer.render(this.physicsWorld, this.camera.combined);
            }
            this.renderLock.unlock();

        }
    }


    /**
     * The the current listener
     *
     * @param sceneListener The listener of the scene
     */
    public void setListener(final SceneListener sceneListener){
        this.sceneListener = sceneListener;
    }

    public void removeListener(){
        this.sceneListener = null;
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.debug("Scene","resize("+width+", "+height+")");
        this.viewport.update(width,height);
    }

    @Override
    public void pause() {
        Gdx.app.debug("Scene","pause()");
        this.paused = true;
    }

    @Override
    public void resume() {
        Gdx.app.debug("Scene","resume()");
        this.paused = false;
    }

    @Override
    public void hide() {
        Gdx.app.debug("Scene","hide()");
        this.paused = true;
        for(final Layer layer : this.layers) {
            layer.hide();
        }
        this.executor.shutdown();
    }

    @Override
    @SuppressWarnings("all")
    public void dispose() {
        Gdx.app.debug("Scene","dispose()");
        this.paused = true;
        this.executor.shutdown();
        this.physicsWorld.dispose();
        for(final Layer layer : this.layers){
            layer.dispose();
        }
    }

    public World getPhysicsWorld() {
        return physicsWorld;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Layer[] getLayers() {
        return layers;
    }

    public List<PhysicsActor> getPhysicsActors() {
        return physicsActors;
    }

    public boolean isPaused() {
        return paused;
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
            public int layers = 1;
            public boolean clearScreen = true;
            public float[] clearColor = {0f, 0f, 0f, 1f};
        }

        /**
         * Physics class for Scene settings
         */
        public static class Physics{
            public float[] gravity = {0.0f,-9.8f};
            public boolean asyncMode = !Gdx.app.getType().equals(Application.ApplicationType.Android);
            public float frequency = 0.01666666666f;
            public int velocityIterations = Gdx.app.getType().equals(Application.ApplicationType.Desktop) ? 8:6;
            public int positionIterations = Gdx.app.getType().equals(Application.ApplicationType.Desktop) ? 3:2;
            public int particleIterations = 1;
            public boolean debug = false;
        }


    }
}
