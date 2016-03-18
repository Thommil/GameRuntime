

package com.thommil.libgdx.runtime.test.test_06_input;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_05_softbody.level.SoftbodyLevel;
import com.thommil.libgdx.runtime.test.test_06_input.level.InputLevel;

/**
 * Input TEST
 *
 * @author  thommil on 3/4/16.
 */
public class InputGame extends Game{

    InputLevel inputLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FIT;
        settings.viewport.width = 40;
        settings.viewport.height = 40;
        settings.physics.enabled = true;
        settings.physics.gravity[0] = settings.physics.gravity[1] = 0;
        //settings.physics.debug=true;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        inputLevel = new InputLevel();
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
