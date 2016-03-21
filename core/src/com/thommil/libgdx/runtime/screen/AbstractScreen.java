package com.thommil.libgdx.runtime.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;

/**
 * Abstract screen definition of runtime screens
 *
 * @author Thommil on 29/02/16.
 */
public abstract class AbstractScreen implements Screen {

    /**
     * Viewport of the screen
     */
    protected Viewport viewport;

    /**
     * Default constructor
     *
     * @param viewport reference to the game viewport to use
     */
    public AbstractScreen(final Viewport viewport){
        this.viewport = viewport;
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        //NOP
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        //NOP
    }

    /**
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {
        //NOP
    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {
        //NOP
    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {
        //NOP
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        //NOP
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        //NOP
    }
}
