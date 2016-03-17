
package com.thommil.libgdx.runtime.test.test_12_layout;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_11_events.level.EventsLevel;
import com.thommil.libgdx.runtime.test.test_12_layout.level.LayoutLevel;

/**
 * Events TEST
 *
 * @author  thommil on 3/4/16.
 */
public class LayoutGame extends Game{

    LayoutLevel layoutLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FIT;
        settings.viewport.width = 16;
        settings.viewport.height = 10;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        layoutLevel = new LayoutLevel();
        showScreen(Runtime.getInstance());
    }

    @Override
    protected void onShowScreen(final Screen screen) {

    }

    @Override
    protected void onResize(int width, int height) {
        layoutLevel.resize();
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
