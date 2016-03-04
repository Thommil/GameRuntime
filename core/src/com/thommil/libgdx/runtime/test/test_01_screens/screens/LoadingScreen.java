package com.thommil.libgdx.runtime.test.test_01_screens.screens;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.screen.AbstractScreen;

/**
 * Loading screen
 *
 * @author Thommil on 3/4/16.
 */
public class LoadingScreen extends AbstractScreen implements com.thommil.libgdx.runtime.screen.LoadingScreen {

    /**
     * Default constructor
     *
     * @param viewport reference to the game viewport to use
     */
    public LoadingScreen(Viewport viewport) {
        super(viewport);
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
     * Indicates the loading progress (0.0 - 1.0)
     *
     * @param progress The loading progress
     */
    @Override
    public void onLoadProgress(float progress) {

    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {

    }
}
