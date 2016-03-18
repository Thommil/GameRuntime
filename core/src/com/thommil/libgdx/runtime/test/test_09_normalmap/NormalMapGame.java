
package com.thommil.libgdx.runtime.test.test_09_normalmap;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_09_normalmap.level.NormalMapLevel;

/**
 * NormalMap TEST
 *
 * @author  thommil on 3/4/16.
 */
public class NormalMapGame extends Game{

    NormalMapLevel normalMapLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.STRECTCH;
        settings.viewport.width = 20;
        settings.viewport.height = 20;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        normalMapLevel = new NormalMapLevel();
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
        this.normalMapLevel.onResize(width, height);
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
