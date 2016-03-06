package com.thommil.libgdx.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.layer.Layer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Main runtime
 *
 * @author Thommil on 29/02/16.
 */
public class Runtime implements Screen{

    /**
     * The runtime settings
     */
    protected final Settings settings;

    /**
     * The attached viewport
     */
    protected Viewport viewport;

    /**
     * Singleton
     */
    private static Runtime instance;

    /**
     * Physics World container
     */
    protected final World physicsWorld;

    /**
     * Inner layers  list
     */
    protected final Array<Layer> layers;

    /**
     * Executor bound to this scene
     */
    protected ExecutorService executor;

    /**
     * Queue for step executor
     */
    private final Deque<Runnable> physicsQueue;

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
    protected boolean paused = true;

    /**
     * Runnning state
     */
    protected boolean running = true;

    /**
     * Create instance based on settings
     *
     * @param settings The runtime settings
     *
     * @return The Runtime instance
     */
    public static Runtime createInstance(final Settings settings){
        if(Runtime.instance == null){
            Runtime.instance = new Runtime(settings);
            Runtime.instance.start();
        }
        return Runtime.instance;
    }

    /**
     * Factory
     *
     * @return The Runtime instance
     */
    public static Runtime getInstance(){
        return Runtime.instance;
    }

    private Runtime(final Settings settings) {
        this.settings = settings;
        if(settings.physics.enabled) {
            this.physicsWorld = new World(new Vector2(settings.physics.gravity[0], settings.physics.gravity[1]), true);
            this.physicsQueue = new ArrayDeque<Runnable>();
        }
        else{
            this.physicsWorld = null;
            this.physicsQueue = null;
        }
        this.layers = new Array<Layer>();

        if(settings.physics.debug){
            this.debugRenderer = new Box2DDebugRenderer();
        }
    }

    /**
     * Set the viewport
     * @param viewport
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    /**
     * Get the viewport
     */
    public Viewport getViewport() {
        return this.viewport;
    }

    /**
     * Get the Physics World
     */
    public World getPhysicsWorld() {
        return this.physicsWorld;
    }

    /**
     * Get the settings (some settings can be modified when started)
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Indicates if the Runtime is in paused state
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Indicates if the Runtime is running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Starts the instance
     */
    private final void start(){
        if(settings.physics.enabled) {
            //Start executor
            executor = Executors.newFixedThreadPool(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    return thread;
                }
            });

            //Aync loop
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    running = true;
                    paused = true;
                    while (running) {
                        final long longAsyncFrequency = (long) (settings.physics.frequency * 1000f);
                        final long start = System.currentTimeMillis();

                        if (!paused) {

                            int tasksCount = settings.physics.maxTasks;
                            while (!physicsQueue.isEmpty() && tasksCount-- > 0) {
                                physicsQueue.poll().run();
                            }

                            final float lastPhysicsStepDurationFloat = (float)lastPhysicsStepDuration/1000f;

                            final Object[] items = ((Object[])layers.items);
                            for(int index=0; index < layers.size; index++){
                                if (items != null) {
                                    ((Layer)items[index]).step(lastPhysicsStepDurationFloat);
                                }
                            }

                            physicsWorld.step(settings.physics.frequency * settings.physics.timeFactor
                                    , settings.physics.velocityIterations
                                    , settings.physics.positionIterations
                                    , settings.physics.particleIterations);

                            lastPhysicsStepDuration = System.currentTimeMillis() - start;

                            if (lastPhysicsStepDuration < longAsyncFrequency) {
                                try {
                                    Thread.currentThread().sleep(longAsyncFrequency - lastPhysicsStepDuration);
                                } catch (InterruptedException ie) {
                                    Gdx.app.exit();
                                }
                            }

                        } else {
                            try {
                                Thread.currentThread().sleep(500);
                            } catch (InterruptedException ie) {
                                Gdx.app.exit();
                            }
                        }

                    }
                }
            });
        }
    }

    /**
     * Add a layer to the runtime at specified index
     *
     * @param index The index (z-index) of the layer
     * @param layer The layer to add
     */
    public void addLayer(final int index, final Layer layer){
        if(!paused) throw new GameRuntimeException("Layers can only be added/removed in paused state");
        while(index > (this.layers.size-1)){
            this.layers.add(null);
        }
        this.layers.set(index, layer);
        layer.bind(this);
        layer.show();
    }

    /**
     * Add a layer to the runtime on top of other layers
     *
     * @param layer The layer to add
     */
    public void addLayer(final Layer layer){
        if(!paused) throw new GameRuntimeException("Layers can only be added/removed in paused state");
        this.layers.add(layer);
        layer.bind(this);
        layer.show();
    }

    /**
     * Removes a layer from the runtime
     *
     * @param layer The layer to remove
     */
    public void removeLayer(final Layer layer){
        this.removeLayer(this.layers.indexOf(layer, true));
    }

    /**
     * Removes a layer from the runtime
     *
     * @param index The index of the layer to remove
     */
    public void removeLayer(final int index){
        if(!paused) throw new GameRuntimeException("Layers can only be added/removed in paused state");
        this.layers.get(index).hide();
        this.layers.get(index).unbind();
        this.layers.set(index, null);
    }

    /**
     * Run a task in Physics Thread
     *
     * @param task The task to run
     */
    public void runOnPhysicsThread(final Runnable task){
        if(settings.physics.enabled) {
            physicsQueue.add(task);
        }
    }

    /**
     * Run a task in UI Thread
     *
     * @param task The task to run
     */
    public void runOnUIThread(final Runnable task){
        Gdx.app.postRunnable(task);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        this.resume();
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if(!paused) {
            final Object[] items = ((Object[]) layers.items);
            for (int index = 0; index < layers.size; index++) {
                if (items != null) {
                    ((Layer) items[index]).render(delta);
                }
            }

            if (this.settings.physics.debug) {
                debugRenderer.render(physicsWorld, viewport.getCamera().combined);
            }
        }
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        final Object[] items = ((Object[])layers.items);
        for(int index=0; index < layers.size; index++){
            if (items != null) {
                ((Layer)items[index]).resize(width, height);
            }
        }
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {
        this.hide();
    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {
        paused = false;
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        paused = true;
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        running=false;
        final Object[] items = ((Object[])layers.items);
        for(int index=0; index < layers.size; index++){
            if (items != null) {
                ((Layer)items[index]).dispose();
            }
        }
        if(this.settings.physics.enabled) {
            executor.shutdown();
        }
    }

    /**
     * Destrou the runtime instance
     */
    public static void destroyInstance(){
        Runtime.instance.dispose();
        Runtime.instance = null;
    }
}
