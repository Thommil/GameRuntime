package com.thommil.libgdx.runtime;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.*;
import com.thommil.libgdx.runtime.screen.LoadingScreen;

/**
 * Main entry point of a application/game instance
 *
 * @author Thommil
 * @version 1.0
 */
public abstract class Game implements ApplicationListener {

    /**
     * Current screen displayed
     */
    private Screen currentScreen;

    /**
     * Next screen displayed
     */
    private Screen nextScreen;

    /**
     * The global settings
     */
    private Settings settings;

    /**
     * The global asset manager
     */
    private AssetManager assetManager;

    /**
     * The global runtime instance
     */
    private Runtime runtime;

    /**
     * Main Viewport of the game
     */
    private Viewport viewport;

    /**
     * Indicates the loading state of the game
     */
    private boolean loading = true;

    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public final void create() {
        this.settings = new Settings();
        this.assetManager = new AssetManager();

        this.onCreate(this.settings);

        this.loading = (this.assetManager.getProgress() < 1.0f);

        switch(this.settings.viewport.type){
            case Settings.Viewport.SCREEN :
                this.viewport = new ScreenViewport();
                break;
            case Settings.Viewport.FILL :
                this.viewport = new FillViewport(this.settings.viewport.width, this.settings.viewport.height);
                break;
            case Settings.Viewport.FIT :
                this.viewport = new FitViewport(this.settings.viewport.width, this.settings.viewport.height);
                break;
            case Settings.Viewport.STRECTCH :
                this.viewport = new StretchViewport(this.settings.viewport.width, this.settings.viewport.height);
                break;
        }

        this.runtime = Runtime.createInstance(this.settings);
        this.runtime.setViewport(this.viewport);

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

        this.onStart(this.viewport);
    }

    /**
     * Display the specified screen. If current displayed screen implements LoadingScreen,
     * it will be called to display loading feedbacks. Subclasses should implement onShowScreen()
     * to prepare the screen to display (assets, logic ...)
     *
     * @param screen The screen to display
     */
    public final void showScreen(final Screen screen){
        this.onShowScreen(screen);
        this.loading = (this.assetManager.getProgress() < 1.0f);
        if(this.loading){
            if(this.currentScreen == null) {
                throw new GameRuntimeException("Current screen cannot be null when AssetManager is loading");
            }
            this.nextScreen = screen;
            if(this.currentScreen instanceof LoadingScreen) {
                this.assetManager.setErrorListener((LoadingScreen) this.currentScreen);
            }
        }
        else{
            if(this.currentScreen != null) {
                this.currentScreen.hide();
            }
            this.currentScreen = screen;
            this.currentScreen.show();
        }
    }

    /**
     * Called when the {@link Application} is resized. This can happen at any point during a non-paused state but will never happen
     * before a call to {@link #create()}.
     *
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public final void resize(int width, int height) {
        this.viewport.update(width, height);
        this.currentScreen.resize(width, height);
    }

    /**
     * Called when the {@link Application} should render itself.
     */
    @Override
    public final void render() {
        if(settings.graphics.clearScreen) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        if(this.loading){
            this.assetManager.update();
            final float progress = this.assetManager.getProgress();
            if(progress == 1.0f){
                this.loading = false;
                this.assetManager.setErrorListener(null);
                this.currentScreen.hide();
                this.currentScreen = this.nextScreen;
                this.currentScreen.show();
            }
            else if(this.currentScreen instanceof LoadingScreen){
                ((LoadingScreen)this.currentScreen).onLoadProgress(progress);
            }
        }
        this.currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    /**
     * Get the current displayed screen
     */
    protected Screen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Get the AssetManager
     */
    protected AssetManager getAssetManager(){
        return assetManager;
    }

    /**
     * Called when the {@link Application} is paused, usually when it's not active or visible on screen. An Application is also
     * paused before it is destroyed.
     */
    @Override
    public final void pause() {
        this.currentScreen.pause();
        this.onPause();
    }

    /**
     * Called when the {@link Application} is resumed from a paused state, usually when it regains focus.
     */
    @Override
    public final void resume() {
        this.currentScreen.resume();
        this.onResume();
    }

    /**
     * Called when the {@link Application} is destroyed. Preceded by a call to {@link #pause()}.
     */
    @Override
    public final void dispose() {
        this.onDispose();
        this.assetManager.clear();
        Runtime.destroyInstance();
    }

    /**
     * API
     */

    /**
     * Called at creation, settings can be modified here, assets can be added too.
     *
     * @param settings The settings of the game
     */
    protected abstract void onCreate(final Settings settings);

    /**
     * Called at game startup, can be used to set the first screen
     *
     * @param viewport The viewport to use
     */
    protected abstract void onStart(final Viewport viewport);

    /**
     * Called when a screen is asked to be displayed, subclasses can add assets and logic here to
     * prepare this screen.
     *
     * @param screen The screen to display
     */
    protected abstract void onShowScreen(final Screen screen);

    /**
     * Called when the game gains the focus
     */
    protected abstract void onResume();

    /**
     * Called when the game lose the focus
     */
    protected abstract void onPause();

    /**
     * Called at end of the game
     */
    protected abstract void onDispose();

}
