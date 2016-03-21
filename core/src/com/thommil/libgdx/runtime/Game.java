package com.thommil.libgdx.runtime;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.*;

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
     * Called when the {@link Application} is first created.
     */
    @Override
    public final void create() {
        this.settings = new Settings();
        this.assetManager = new AssetManager();

        this.onCreate(this.settings);

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
     * Display the specified screen.
     *
     * @param screen The screen to display
     */
    public final void showScreen(final Screen screen){
        if(this.currentScreen != null){
            if(this.currentScreen == this.runtime){
                this.onHideRuntime();
            }
            this.currentScreen.hide();
        }

        this.currentScreen = screen;

        if(this.currentScreen == this.runtime){
            this.onShowRuntime();
        }
        this.currentScreen.show();

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
        this.onResize(width, height);
        if(this.currentScreen != this.runtime){
            this.runtime.resize(width, height);
        }
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

        this.currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    /**
     * Get the current displayed screen
     */
    protected Screen getCurrentScreen() {
        return this.currentScreen;
    }

    /**
     * Get the AssetManager
     */
    protected AssetManager getAssetManager(){
        return this.assetManager;
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
     * Called the runtime is about to be displayed.
     */
    protected abstract void onShowRuntime();

    /**
     * Called the runtime is about to be hidden.
     */
    protected abstract void onHideRuntime();

    /**
     * Called when screen is resized
     *
     * @param width the new screen width
     * @param height the new screen height
     */
    protected abstract void onResize(int width, int height);

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
