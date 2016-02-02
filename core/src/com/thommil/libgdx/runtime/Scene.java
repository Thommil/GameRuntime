package com.thommil.libgdx.runtime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.actor.Actor;
import com.thommil.libgdx.runtime.actor.Renderable;

import java.util.ArrayList;
import java.util.List;

/**
 * Main container for Graphics and Physics actors.
 * <br/><br/>
 * This scene is by default handling physics and should be rewritten to
 * deactivate physics support.
 *
 * Created by thommil on 01/02/16.
 */
public class Scene implements Screen {

    /**
     * Physics World container
     */
    protected final World physicsWorld;

    /**
     * Viewport of the scene
     */
    private Viewport viewport;

    /**
     * Inner actors list
     */
    protected final List<Actor> actors;

    /**
     * Inner renderable list
     */
    protected final List<Renderable> renderables;

    /**
     * Default constructor using Settings
     *
     */
    public Scene(Scene.Settings settings) {
        Gdx.app.debug("Scene","New scene");
        this.actors = new ArrayList<Actor>();
        this.renderables = new ArrayList<Renderable>();
        this.physicsWorld = new World(new Vector2(settings.gravity.x, settings.gravity.y), true);
        this.viewport = new ExtendViewport(settings.viewport.minWorldWidth,settings.viewport.minWorldHeight
                                            ,settings.viewport.maxWorldWidth, settings.viewport.maxWorldHeight
                                            ,new OrthographicCamera());
        this.viewport.apply(settings.viewport.centerCamera);
    }


    @Override
    public void show() {
        Gdx.app.debug("Scene","show()");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    public void dispose() {
        Gdx.app.debug("Scene","dispose()");
        this.physicsWorld.dispose();
        this.actors.clear();
        this.renderables.clear();
    }

    /**
     * Raw object to define scene properties
     */
    public static class Settings{

        /**
         * Gravity settings
         */
        public Gravity gravity = new Gravity();

        /**
         * Viewport settings
         */
        public Viewport viewport = new Viewport();

        /**
         * Gravity class for Scene settings
         */
        public static class Gravity{
            public float x = 0.0f;
            public float y = -9.8f;
        }

        /**
         * Viewport class for Scene settings.<br/>
         * To be efficient with physics engine, think in meters :)
         */
        public static class Viewport{
            public float minWorldWidth = Gdx.graphics.getWidth();
            public float minWorldHeight = Gdx.graphics.getHeight();
            public float maxWorldWidth = 0;
            public float maxWorldHeight = 0;
            public boolean centerCamera = true;
        }

    }
}
