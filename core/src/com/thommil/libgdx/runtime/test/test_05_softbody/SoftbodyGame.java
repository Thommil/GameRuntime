

package com.thommil.libgdx.runtime.test.test_05_softbody;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_05_softbody.level.SoftbodyLevel;

/**
 * Softbody TEST
 *
 * @author  thommil on 3/4/16.
 */
public class SoftbodyGame extends Game{

    SoftbodyLevel softbodyLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FIT;
        settings.viewport.width = 40;
        settings.viewport.height = 40;
        settings.physics.enabled = true;
        settings.physics.particleIterations = 8;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        softbodyLevel = new SoftbodyLevel();
        this.showScreen(Runtime.getInstance());
    }

    @Override
    protected void onShowScreen(final Screen screen) {

    }

    @Override
    protected void onShowRuntime() {

    }

    @Override
    protected void onResize(int width, int height) {

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
