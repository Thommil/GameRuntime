
package com.thommil.libgdx.runtime.test.test_04_rigidbody;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_04_rigidbody.level.RigidbodyLevel;

/**
 * Rigidbody TEST
 *
 * @author  thommil on 3/4/16.
 */
public class RigidbodyGame extends Game{

    RigidbodyLevel rigidbodyLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FILL;
        settings.viewport.width = 200;
        settings.viewport.height = 100;
        settings.physics.enabled = true;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        rigidbodyLevel = new RigidbodyLevel();
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
