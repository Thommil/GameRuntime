package com.thommil.libgdx.runtime.test.test_02_spritecache;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_02_spritecache.levels.CacheLevel;

/**
 * Screens worlflow TEST
 *
 * @author  thommil on 3/4/16.
 */
public class SpriteCacheGame extends Game{

    CacheLevel cacheLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FIT;
        settings.viewport.width = 10;
        settings.viewport.height = 10;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        cacheLevel = new CacheLevel();
        this.showScreen(Runtime.getInstance());
    }

    @Override
    protected void onShowScreen(final Screen screen) {

    }

    @Override
    protected void onResume() {

    }

    @Override
    protected void onPause() {

    }

    @Override
    protected void onDispose() {

    }
}
