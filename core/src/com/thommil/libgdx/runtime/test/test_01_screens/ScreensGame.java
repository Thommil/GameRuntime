package com.thommil.libgdx.runtime.test.test_01_screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_01_screens.screens.SplashScreen;

/**
 * Screens worlflow TEST
 *
 * @author  thommil on 3/4/16.
 */
public class ScreensGame extends Game {

    Screen splashScreen;

    @Override
    protected void onCreate(Settings settings, AssetManager assetManager) {
        settings.viewport.type = Settings.Viewport.FILL;
        settings.viewport.width = 100;
        settings.viewport.height = 100;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        this.splashScreen = new SplashScreen(viewport);
        this.showScreen(splashScreen);
    }

    @Override
    protected void onShowScreen(Screen screen, AssetManager assetManager) {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onPause() {

    }

    @Override
    protected void onDispose() {
        this.splashScreen.dispose();
    }
}
