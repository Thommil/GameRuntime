package com.thommil.libgdx.runtime.runtime;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.*;
import com.thommil.libgdx.runtime.runtime.screen.DefaultLoadingScreen;
import com.thommil.libgdx.runtime.runtime.screen.LoadingScreen;
import com.thommil.libgdx.runtime.runtime.screen.Runtime;

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
    protected Screen currentScreen;

    /**
     * The global settings
     */
    protected Settings settings;

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
     * Default loading screen to use of not overriden
     */
    private LoadingScreen defaultLoadingScreen;

    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public final void create() {
        this.settings = new Settings();
        this.assetManager = new AssetManager();

        this.onCreate(this.settings, this.assetManager);
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

        final Screen splashScreen = this.getSplashScreen(this.viewport);
        if(splashScreen != null) {
            this.showScreen(splashScreen, false);
        }
        else{
            final Screen homeScreen = this.getHomeScreen(this.viewport);
            if(homeScreen == null){
                throw new GameRuntimeException("No homescreen found");
            }
            this.showScreen(homeScreen, true);
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
        this.runtime.resize(width, height);
    }

    /**
     * Called when the {@link Application} should render itself.
     */
    @Override
    public final void render() {
        this.currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public final void showScreen(final Screen screen, final boolean loadingScreen){
        //Not loaded
        /*if(loadingScreen && this.assetManager.getProgress() < 1f){
            this.currentScreen = this.getLoadingScreen(this.viewport);
        }
        else
        if(this.assetManager.getProgress() < 1f){
           this.currentScreen = sc
        }*/
    }

    public final void showLevel(final int levelId, final boolean loadingScreen){

    }

    /**
     * Called when the {@link Application} is paused, usually when it's not active or visible on screen. An Application is also
     * paused before it is destroyed.
     */
    @Override
    public final void pause() {

    }

    /**
     * Called when the {@link Application} is resumed from a paused state, usually when it regains focus.
     */
    @Override
    public final void resume() {

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
     * Raw object to define scene properties
     */
    public static class Settings{

        /**
         * Core settings
         */
        public final Core core = new Core();

        /**
         * Viewport settings
         */
        public final Viewport viewport = new Viewport();

        /**
         * Physics settings
         */
        public final Physics physics = new Physics();

        /**
         * Gravity settings
         */
        public final Graphics graphics = new Graphics();

        /**
         * User Data object for passing data across screens and levels
         */
        public Object userData = null;

        /**
         * Core settings class
         */
        public static class Core{
            public int initialCapacity = 100;
        }

        /**
         * Viewport class for Scene settings.<br/>
         * To be efficient with physics engine, think in meters :)
         */
        public static class Viewport{
            public static final int SCREEN = 0;
            public static final int FILL = 1;
            public static final int FIT = 2;
            public static final int STRECTCH = 3;
            public int type = SCREEN;
            public float width = Gdx.graphics.getWidth();
            public float height = Gdx.graphics.getHeight();
        }

        /**
         * Renderer class for Scene settings
         */
        public static class Graphics{
            public boolean clearScreen = true;
            public float[] clearColor = {0f, 0f, 0f, 1f};
            public boolean depthMaskEnabled = false;
            public boolean blendEnabled = true;
            public int blendSrcFunc = GL20.GL_SRC_ALPHA;
            public int blendDstFunc = GL20.GL_ONE_MINUS_SRC_ALPHA;
        }

        /**
         * Physics class for Scene settings
         */
        public static class Physics{
            public float[] gravity = {0.0f,-9.8f};
            public float frequency = 1/100f;
            public float timeFactor = 1f;
            public int velocityIterations = 8;
            public int positionIterations = 3;
            public int particleIterations = 3;
            public boolean debug = false;
        }
    }

    /**
     * API
     */

    /**
     * Gets the splash screen if any
     *
     * @return The splash screen instance
     */
    protected Screen getSplashScreen(final Viewport viewport){
        return null;
    }


    /**
     * Gets the loading screen if any
     *
     * @return The loading screen
     */
    protected LoadingScreen getLoadingScreen(final Viewport viewport){
        if(this.defaultLoadingScreen == null){
            this.defaultLoadingScreen = new DefaultLoadingScreen(this.viewport, this.assetManager);
        }
        return this.defaultLoadingScreen;
    }

    protected abstract void onCreate(final Settings settings, final AssetManager assetManager);

    protected abstract Screen getHomeScreen(final Viewport viewport);

    protected abstract void onShowScreen(final Screen screen, final AssetManager assetManager);

    protected abstract void onShowLevel(final int levelId, final AssetManager assetManager);

    protected abstract void onPause();

    protected abstract void onResume();

    protected abstract void onDispose();

}
