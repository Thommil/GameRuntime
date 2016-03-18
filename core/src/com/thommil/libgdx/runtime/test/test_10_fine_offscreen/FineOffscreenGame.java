
package com.thommil.libgdx.runtime.test.test_10_fine_offscreen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_10_fine_offscreen.level.FineOffscreenLevel;

/**
 * Fine offscreen rendering TEST
 *
 * @author  thommil on 3/4/16.
 */
public class FineOffscreenGame extends Game{

    FineOffscreenLevel fineOffscreenLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.STRECTCH;
        settings.viewport.width = 100;
        settings.viewport.height = 100;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        fineOffscreenLevel = new FineOffscreenLevel();
        this.showScreen(Runtime.getInstance());
    }

    @Override
    protected void onShowRuntime() {

    }

    @Override
    protected void onHideRuntime() {

    }

    @Override
    protected void onResize(int width, int height) {
        this.fineOffscreenLevel.onResize(width, height);
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
