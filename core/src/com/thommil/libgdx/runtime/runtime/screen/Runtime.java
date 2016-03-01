package com.thommil.libgdx.runtime.runtime.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.runtime.Game;

/**
 * Created by tomtom on 29/02/16.
 */
public class Runtime implements Screen{

    /**
     * The runtime settings
     */
    protected final Game.Settings settings;

    /**
     * The attached viewport
     */
    protected Viewport viewport;

    /**
     * Singleton
     */
    private static Runtime instance;

    /**
     * Create instance based on settings
     *
     * @param settings The runtime settings
     *
     * @return The Runtime instance
     */
    public static Runtime createInstance(final Game.Settings settings){
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

    /**
     *
     * @param settings
     */
    private Runtime(final Game.Settings settings) {
        this.settings = settings;
    }

    /**
     * Set the viewport
     * @param viewport
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    /**
     * Starts the instance
     */
    private final void start(){

    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {

    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }
}
