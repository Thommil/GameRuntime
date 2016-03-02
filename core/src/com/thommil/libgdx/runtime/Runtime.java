package com.thommil.libgdx.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
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
 * Created by tomtom on 29/02/16.
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

        //GL Settings
        Gdx.gl.glClearColor(this.settings.graphics.clearColor[0]
                , this.settings.graphics.clearColor[1]
                , this.settings.graphics.clearColor[2]
                , this.settings.graphics.clearColor[3]);

        Gdx.gl.glDepthMask(this.settings.graphics.depthMaskEnabled);

        if (this.settings.graphics.blendEnabled) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(this.settings.graphics.blendSrcFunc, this.settings.graphics.blendDstFunc);
        } else {
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

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
                    paused = false;
                    while (true) {
                        final long longAsyncFrequency = (long) (settings.physics.frequency * 1000f);
                        final long start = System.currentTimeMillis();

                        if (!paused) {

                            int tasksCount = settings.physics.maxTasks;

                            while (!physicsQueue.isEmpty() && tasksCount-- > 0) {
                                physicsQueue.poll().run();
                            }

                            final float lastPhysicsStepDurationFloat = (float)lastPhysicsStepDuration/1000f;

                            for (final Layer layer : layers) {
                                if (layer != null) {
                                    layer.step(lastPhysicsStepDurationFloat);
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
     * Add a layer to the runtime
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
        this.layers.set(index, null);
    }

    /**
     * Run a task in Physics Thread
     *
     * @param task The task to run
     */
    public void runOnPhysicsThread(final Runnable task){
        physicsQueue.add(task);
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
        for (final Layer layer : layers) {
            if (layer != null) {
                layer.show();
            }
        }
        paused = false;
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if(settings.graphics.clearScreen) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        for (final Layer layer : layers) {
            if (layer != null) {
                layer.render(delta);
            }
        }

        if(this.settings.physics.debug){
            debugRenderer.render(physicsWorld, viewport.getCamera().combined);
        }
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        for (final Layer layer : layers) {
            if (layer != null) {
                layer.resize(width, height);
            }
        }
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {
        paused = true;
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
        for (final Layer layer : layers) {
            if (layer != null) {
                layer.hide();
            }
        }
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        for (final Layer layer : layers) {
            if (layer != null) {
                layer.dispose();
            }
        }
        executor.shutdownNow();
    }
}
