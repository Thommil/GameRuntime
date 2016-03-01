package com.thommil.libgdx.runtime.test.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.runtime.Game;
import com.thommil.libgdx.runtime.runtime.screen.LoadingScreen;
import com.thommil.libgdx.runtime.runtime.screen.Runtime;

/**
 * Created by tomtom on 29/02/16.
 */
public class GameTest extends Game {

    @Override
    protected Screen getSplashScreen(Viewport viewport) {
        return super.getSplashScreen(viewport);
    }

    @Override
    protected Screen getHomeScreen(Viewport viewport) {
        return null;
    }

    @Override
    protected LoadingScreen getLoadingScreen(Viewport viewport) {
        return super.getLoadingScreen(viewport);
    }

    @Override
    protected void onCreate(Settings settings, AssetManager assetManager) {

    }

    @Override
    protected void onShowScreen(Screen screen, AssetManager assetManager) {

    }

    @Override
    protected void onShowLevel(int level, AssetManager assetManager) {

    }

    @Override
    protected void onPause() {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onDispose() {

    }
}
