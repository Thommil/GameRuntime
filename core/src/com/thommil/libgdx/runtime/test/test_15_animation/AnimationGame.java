
package com.thommil.libgdx.runtime.test.test_15_animation;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_15_animation.level.AnimationLevel;

/**
 * Animation TEST
 *
 * @author  thommil on 3/4/16.
 */
public class AnimationGame extends Game{

    AnimationLevel animationLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.STRECTCH;
        settings.viewport.width = 10;
        settings.viewport.height = 10;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        animationLevel = new AnimationLevel();
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
