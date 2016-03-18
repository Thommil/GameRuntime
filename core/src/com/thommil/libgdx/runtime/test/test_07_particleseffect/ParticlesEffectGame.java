

package com.thommil.libgdx.runtime.test.test_07_particleseffect;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thommil.libgdx.runtime.Game;
import com.thommil.libgdx.runtime.Runtime;
import com.thommil.libgdx.runtime.Settings;
import com.thommil.libgdx.runtime.test.test_07_particleseffect.level.ParticlesEffectLevel;

/**
 * ParticlesEffect TEST
 *
 * @author  thommil on 3/4/16.
 */
public class ParticlesEffectGame extends Game{

    ParticlesEffectLevel particlesEffectLevel;

    @Override
    protected void onCreate(Settings settings) {
        settings.viewport.type = Settings.Viewport.FILL;
        settings.viewport.width = 5;
        settings.viewport.height = 5;
        settings.graphics.blendDstFunc = GL20.GL_ONE;
        settings.physics.enabled = false;
    }

    @Override
    protected void onStart(final Viewport viewport) {
        particlesEffectLevel = new ParticlesEffectLevel();
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
