
package com.thommil.libgdx.runtime.test.test_13_rube;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_12_layout.level.LayoutLevel;
import com.thommil.libgdx.runtime.test.test_13_rube.level.RubeLevel;

/**
 * Events TEST
 *
 * @author  thommil on 3/4/16.
 */
public class RubeGame extends Game{

    RubeLevel rubeLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.STRECTCH;
        settings.viewport.width = 40;
        settings.viewport.height = 20;
        settings.physics.enabled = true;
        //settings.physics.debug = true;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        rubeLevel = new RubeLevel();
        showScreen(Runtime.getInstance());
    }

    @Override
    protected void onShowRuntime() {

    }

    @Override
    protected void onHideRuntime() {

    }

    @Override
    protected void onResize(int width, int height) {
        rubeLevel.resize(width, height);
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
