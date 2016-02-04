package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.physics.Physicable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Main container for Actors.
 *
 * Created by thommil on 01/02/16.
 */
@SuppressWarnings("unused")
public class Scene implements Screen {

    /**
     * Inner constant for async mode update
     */
    private final static int UPDATE = 0;

    /**
     * Inner constant for async mode commit
     */
    private final static int COMMIT = 1;

    /**
     * Scene settings
     */
    private final Scene.Settings settings;

    /**
     * Physics World container
     */
    protected final World physicsWorld;

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
     * Inner actors list
     */
    protected final List<Actor> actors;

    /**
     * Inner Physicable actors list
     */
    protected final List<Physicable> physicables;

    /**
     * Executor bound to this scene
     */
    protected ExecutorService executor;

    /**
     * Pause/resume state
     */
    protected boolean paused;

    /**
     * Default constructor using Settings
     *
     * @param settings The Scene settings
     */
    @SuppressWarnings("all")
    public Scene(final Scene.Settings settings) {
        Gdx.app.debug("Scene","New scene");
        this.settings = settings;

        this.actors = new ArrayList<Actor>();
        this.layers = new Layer[settings.renderer.layers];
        this.physicables = new ArrayList<Physicable>();

        this.physicsWorld = new World(new Vector2(settings.physics.gravity[0], settings.physics.gravity[1]), true);

        this.camera = new OrthographicCamera();
        this.viewport = new ExtendViewport(settings.viewport.minWorldWidth,settings.viewport.minWorldHeight,this.camera);
        this.viewport.apply(settings.viewport.centerCamera);
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
    public void addActor(final Actor actor){
        Gdx.app.debug("Scene","addActor()");
        this.actors.add(actor);
        if(actor instanceof Renderable){
            this.layers[((Renderable)actor).getLayer()].addRenderable((Renderable)actor);
        }
        if(actor instanceof Physicable){
            ((Physicable) actor).getBody().setUserData(actor);
            //Only add to scene if not static
            if(((Physicable) actor).getBodyType() != Physicable.STATIC) {
                this.physicables.add((Physicable) actor);
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
        this.actors.remove(actor);
        if(actor instanceof Renderable){
            this.layers[((Renderable)actor).getLayer()].removeRenderable((Renderable)actor);
        }
        if(actor instanceof Physicable){
            this.physicables.remove(actor);
        }
        actor.dispose();
    }

    @Override
    @SuppressWarnings("all")
    public void show() {
        Gdx.app.debug("Scene","show()");
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

                final long longAsyncFrequency = (long)(Scene.this.settings.physics.asyncFrequency * 1000f);

                @Override
                public void run() {
                    final long start = System.currentTimeMillis();
                    if(!Scene.this.paused){
                        Scene.this.physicsWorld.step(Scene.this.settings.physics.asyncFrequency
                                , Scene.this.settings.physics.velocityIterations
                                , Scene.this.settings.physics.positionIterations
                                , Scene.this.settings.physics.particleIterations);

                        Scene.this.updateAndCommit(UPDATE);
                    }
                    final long duration = System.currentTimeMillis() - start;

                    if(duration > 0){
                        try {
                            Thread.currentThread().sleep(longAsyncFrequency - duration);
                        }catch(InterruptedException ie){
                            Gdx.app.exit();
                        }
                    }
                }
            });

            this.updateAndCommit(UPDATE);
        }
        this.paused = false;
    }

    @Override
    @SuppressWarnings("all")
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
            this.physicsWorld.step(Gdx.graphics.getDeltaTime()
                    , this.settings.physics.velocityIterations
                    , this.settings.physics.positionIterations
                    , this.settings.physics.particleIterations);

            for(final Physicable physicable : this.physicables){
                final Actor actor = ((Actor) physicable);
                final Body body = physicable.getBody();
                final Vector2 position = body.getPosition();
                actor.renderComponents[Actor.X] = position.x;
                actor.renderComponents[Actor.Y] = position.y;
                actor.renderComponents[Actor.ANGLE] = body.getAngle();
            }
        }
        //Async step
        else {
            this.updateAndCommit(COMMIT);
        }

        //Render
        for(final Layer layer : this.layers){
            if(layer.isVisible()) {
                layer.render(delta);
            }
        }
    }

    /**
     * Inner methdd to synchronize physics and rendering
     *
     *  @param action The synchronization action
     */
    private synchronized void updateAndCommit(final int action){
        switch(action){
            case UPDATE :
                for(final Physicable physicable : this.physicables){
                    final Actor actor = ((Actor) physicable);
                    final Body body = physicable.getBody();
                    final Vector2 position = body.getPosition();
                    actor.stepComponents[Actor.X] = position.x;
                    actor.stepComponents[Actor.Y] = position.y;
                    actor.stepComponents[Actor.ANGLE] = body.getAngle();
                }
                break;
            case COMMIT :
                for (final Physicable physicable : this.physicables) {
                    final Actor actor = ((Actor) physicable);
                    actor.renderComponents[Actor.X] = actor.stepComponents[Actor.X];
                    actor.renderComponents[Actor.Y] = actor.stepComponents[Actor.Y];
                    actor.renderComponents[Actor.ANGLE] = actor.stepComponents[Actor.ANGLE];
                }
                break;
        }
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
        for(final Layer layer : this.layers) {
            layer.hide();
        }
        this.executor.shutdown();
    }

    @Override
    @SuppressWarnings("all")
    public void dispose() {
        Gdx.app.debug("Scene","dispose()");
        this.physicsWorld.dispose();
        this.physicables.clear();
        for(final Actor actor : this.actors){
            actor.dispose();
        }
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

    public List<Actor> getActors() {
        return actors;
    }

    public Layer[] getLayers() {
        return layers;
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
            public boolean asyncMode = true;
            public float asyncFrequency = 1/60f;
            public float[] gravity = {0.0f,-9.8f};
            public int velocityIterations = 8;
            public int positionIterations = 3;
            public int particleIterations = 1;
        }


    }
}
