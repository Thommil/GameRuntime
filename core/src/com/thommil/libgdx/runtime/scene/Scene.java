package com.thommil.libgdx.runtime.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.graphics.Renderable;
import com.thommil.libgdx.runtime.physics.Physicable;

import java.util.ArrayList;
import java.util.List;

/**
 * Main container for Actors.
 *
 * Created by thommil on 01/02/16.
 */
public class Scene implements Screen {

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

        this.physicsWorld = new World(new Vector2(settings.gravity.x, settings.gravity.y), true);

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
        this.layers[index] = layer;
        this.showLayer(index);
    }

    /**
     * Enables/shows a layer
     *
     * @param index The index of the layer
     */
    public void showLayer(final int index){
        this.layers[index].setCamera(this.camera);
        this.layers[index].show();
    }

    /**
     * Disables/hides a layer
     *
     * @param index The index of the layer
     */
    public void hideLayer(final int index){
        this.layers[index].hide();
    }

    /**
     * Adds an actor to the Scene
     *
     * @param actor The Actor to add
     */
    public void addActor(final Actor actor){
        this.actors.add(actor);
        if(actor instanceof Renderable){
            this.layers[((Renderable)actor).getLayer()].addRenderable((Renderable)actor);
        }
        if(actor instanceof Physicable){
            ((Physicable) actor).getBody().setUserData(actor);
            this.physicables.add((Physicable)actor);
        }
    }

    /**
     * Removes an actor from the Scene
     *
     * @param actor The Actor to remove
     */
    public void removeActor(final Actor actor){
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
    public void show() {
        Gdx.app.debug("Scene","show()");
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

        for(final Physicable actor : this.physicables){
            if(actor.getBodyType() != Physicable.STATIC) {
                ((Actor) actor).commit();
            }
        }

        for(final Layer layer : this.layers){
            if(layer.isVisible()) {
                layer.render(delta);
            }
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
    }

    @Override
    public void resume() {
        Gdx.app.debug("Scene","resume()");
    }

    @Override
    public void hide() {
        Gdx.app.debug("Scene","hide()");
    }

    @Override
    @SuppressWarnings("all")
    public void dispose() {
        Gdx.app.debug("Scene","dispose()");
        this.physicsWorld.dispose();
        this.physicables.clear();
        for(final Layer layer : this.layers){
            layer.dispose();
        }
        for(final Actor actor : this.actors){
            actor.dispose();
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
         * Viewport settings
         */
        public Viewport viewport = new Viewport();

        /**
         * Gravity settings
         */
        public Gravity gravity = new Gravity();

        /**
         * Gravity settings
         */
        public Renderer renderer = new Renderer();

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
         * Gravity class for Scene settings
         */
        public static class Gravity{
            public float x = 0.0f;
            public float y = -9.8f;
        }


    }
}
