package com.thommil.libgdx.runtime;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
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

        FileHandleResolver resolver = new InternalFileHandleResolver();
        this.assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        this.assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        this.onCreate(this.settings, this.assetManager);

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
        this.onStart();
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
        this.assetManager.clear();
        this.runtime.dispose();
        this.onDispose();
    }

    /**
     * API
     */

    public final void showScreen(final Screen screen){
        this.onShowScreen(screen, this.assetManager);
        this.loading = (this.assetManager.getProgress() < 1.0f);
        if(this.loading){
            this.nextScreen = screen;
            if(this.currentScreen instanceof LoadingScreen) {
                this.assetManager.setErrorListener((LoadingScreen) this.currentScreen);
            }
        }
        else{
            this.currentScreen.hide();
            this.currentScreen = screen;
            this.currentScreen.show();
        }
    }

    protected abstract void onCreate(final Settings settings, final AssetManager assetManager);

    protected abstract void onStart();

    protected abstract void onShowScreen(final Screen screen, final AssetManager assetManager);

    protected abstract void onResume();

    protected abstract void onPause();

    protected abstract void onDispose();

}
