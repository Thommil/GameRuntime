package com.thommil.libgdx.runtime.test.test_01_screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_01_screens.screens.LoadingScreen;
import com.thommil.libgdx.runtime.test.test_01_screens.screens.MainScreen;
import com.thommil.libgdx.runtime.test.test_01_screens.levels.SampleLevel;
import com.thommil.libgdx.runtime.test.test_01_screens.screens.SplashScreen;

/**
 * Screens worlflow TEST
 *
 * @author  thommil on 3/4/16.
 */
public class ScreensGame extends Game implements InputProcessor, ScreensGameAPI{

    Screen nextScreen = null;

    Screen splashScreen;
    LoadingScreen loadingScreen;
    MainScreen mainScreen;
    SampleLevel sampleLevel;

    @Override
    public void load() {
        this.getAssetManager().update();
    }

    @Override
    public float getLoadingProgress() {
        return this.getAssetManager().getProgress();
    }

    @Override
    public void onLoad() {
        if(sampleLevel == null){
            this.sampleLevel = new SampleLevel();
        }
        this.showScreen(nextScreen);
    }

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FILL;
        settings.viewport.width = 600;
        settings.viewport.height = 600;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        this.splashScreen = new SplashScreen(viewport);
        this.loadingScreen = new LoadingScreen(viewport, this);
        this.mainScreen = new MainScreen(viewport);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                showScreen(loadingScreen);
                getAssetManager().load("big_textures/stone.png", Texture.class);
                getAssetManager().load("big_textures/lether.png", Texture.class);
                nextScreen = Runtime.getInstance();
            }
        },1);
        this.showScreen(this.splashScreen);
    }


    @Override
    protected void onShowRuntime() {

    }

    @Override
    protected void onHideRuntime() {

    }

    @Override
    protected void onResize(int width, int height) {
        if(this.loadingScreen != null) this.loadingScreen.resize(width, height);
        if(this.mainScreen != null) this.mainScreen.resize(width, height);
    }

    @Override
    protected void onResume() {
        this.showScreen(Runtime.getInstance());
    }

    @Override
    protected void onPause() {
        this.showScreen(mainScreen);
    }

    /**
     * Called when the screen was touched or a mouse button was pressed. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Start runtime
        if(this.getCurrentScreen() == mainScreen){
            showScreen(loadingScreen);
            getAssetManager().clear();
            getAssetManager().load("big_textures/stone.png", Texture.class);
            getAssetManager().load("big_textures/lether.png", Texture.class);
            nextScreen = Runtime.getInstance();
        }
        else if(this.getCurrentScreen() == Runtime.getInstance()){
            this.showScreen(mainScreen);
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK && this.getCurrentScreen() == Runtime.getInstance()){
            this.showScreen(mainScreen);
        }
        return false;
    }

    @Override
    protected void onDispose() {
        this.splashScreen.dispose();
        this.mainScreen.dispose();
        this.loadingScreen.dispose();
    }


    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }



    /**
     * Called when a finger was lifted or a mouse button was released. The button parameter will be {@link Buttons#LEFT} on iOS.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button   @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.  @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
